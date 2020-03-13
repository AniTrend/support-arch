package co.anitrend.arch.ui.recycler.adapter

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.*
import co.anitrend.arch.core.presenter.SupportPresenter
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.getLayoutInflater
import co.anitrend.arch.theme.animator.contract.ISupportAnimator
import co.anitrend.arch.ui.R
import co.anitrend.arch.ui.action.contract.ISupportActionMode
import co.anitrend.arch.ui.recycler.adapter.contract.ISupportViewAdapter
import co.anitrend.arch.ui.recycler.common.SupportFooterErrorViewHolder
import co.anitrend.arch.ui.recycler.common.SupportFooterLoadingViewHolder
import co.anitrend.arch.ui.recycler.holder.SupportViewHolder
import co.anitrend.arch.ui.util.SupportStateLayoutConfiguration
import timber.log.Timber

/**
 * Core implementation for handling complex logic for [List]s and
 * [androidx.recyclerview.widget.RecyclerView.ViewHolder] binding logic
 *
 * @since v1.2.0
 * @see SupportViewHolder
 */
abstract class SupportListAdapter<T>(
    protected val presenter: SupportPresenter<*>,
    override val stateConfiguration: SupportStateLayoutConfiguration,
    itemCallback: DiffUtil.ItemCallback<T> = ISupportViewAdapter.getDefaultDiffItemCallback()
) : ISupportViewAdapter<T>, RecyclerView.Adapter<SupportViewHolder<T>>() {

    private val mDiffer by lazy {
        AsyncListDiffer<T>(this, itemCallback)
    }

    override val moduleTag: String = javaClass.name

    /**
     * Invokes a filter to search for data on the current adapter
     */
    override val searchQuery = MutableLiveData<String>()

    override var lastAnimatedPosition: Int = 0

    /**
     * Get currently set animation type for recycler view holder items
     *
     * @see [ISupportAnimator]
     */
    override var customSupportAnimator: ISupportAnimator? = null

    /**
     * Retry click interceptor for recycler footer error
     */
    override lateinit var retryFooterAction: View.OnClickListener

    /**
     * Assigned if the current adapter supports needs to support [ISupportActionMode]
     */
    override var supportAction: ISupportActionMode<T>? = null
        set(value) {
            field = value?.apply {
                setRecyclerViewAdapter(this@SupportListAdapter)
            }
        }

    /**
     * Network state which will be used by [co.anitrend.arch.ui.recycler.common.SupportFooterLoadingViewHolder] or
     * [co.anitrend.arch.ui.recycler.common.SupportFooterErrorViewHolder]
     */
    override var networkState: NetworkState? = null
        set(value) {
            val previousState = field
            val hadExtraRow = hasExtraRow()
            field = value
            val hasExtraRow = hasExtraRow()
            when {
                hadExtraRow != hasExtraRow -> {
                    if (hadExtraRow)
                        notifyItemRemoved(itemCount)
                    else
                        notifyItemInserted(itemCount)
                }
                hasExtraRow && previousState != field ->
                    notifyItemChanged(itemCount - 1)
            }
        }

    /**
     * Return the stable ID for the item at [position]. If [hasStableIds]
     * would return false this method should return [RecyclerView.NO_ID].
     *
     * The default implementation of this method returns [RecyclerView.NO_ID].
     *
     * @param position Adapter position to query
     * @return the stable ID of the item at position
     */
    override fun getItemId(position: Int): Long {
        return when (hasStableIds()) {
            true -> getStableIdFor(getItem(position))
            else -> super.getItemId(position)
        }
    }

    /**
     * Overridden implementation creates a default loading footer view when the [viewType] is
     * [R.layout.support_layout_state_footer_loading] or [R.layout.support_layout_state_footer_error]
     * otherwise [createDefaultViewHolder] is called to resolve the view holder type
     */
    override fun onCreateViewHolder(parent: ViewGroup, @LayoutRes viewType: Int): SupportViewHolder<T> {
        val layoutInflater = parent.context.getLayoutInflater()
        return when (viewType) {
            R.layout.support_layout_state_footer_loading -> {
                return SupportFooterLoadingViewHolder(
                    layoutInflater.inflate(
                        R.layout.support_layout_state_footer_loading, parent, false
                    ), stateConfiguration
                )
            }
            R.layout.support_layout_state_footer_error -> {
                return SupportFooterErrorViewHolder(
                    layoutInflater.inflate(
                        R.layout.support_layout_state_footer_error, parent, false
                    ), networkState, retryFooterAction, stateConfiguration
                )
            }
            else -> createDefaultViewHolder(parent, viewType, layoutInflater)
        }
    }

    /**
     * Called when a view created by this adapter has been attached to a window.
     *
     * This can be used as a reasonable signal that the view is about to be seen
     * by the user. If the adapter previously freed any resources in [onViewDetachedFromWindow]
     * those resources should be restored here.
     *
     * @param holder Holder of the view being attached
     */
    override fun onViewAttachedToWindow(holder: SupportViewHolder<T>) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.layoutParams?.also {
            when (it is StaggeredGridLayoutManager.LayoutParams) {
                true -> setLayoutSpanSize(it, holder.adapterPosition)
            }
        }
    }

    /**
     * Called when a view created by this adapter has been detached from its window.
     *
     * <p>Becoming detached from the window is not necessarily a permanent condition;
     * the consumer of an Adapter's views may choose to cache views offscreen while they
     * are not visible, attaching and detaching them as appropriate.</p>
     *
     * @param holder Holder of the view being detached
     */
    override fun onViewDetachedFromWindow(holder: SupportViewHolder<T>) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    /**
     * Called by RecyclerView when it starts observing this Adapter.
     *
     * Keep in mind that same adapter may be observed by multiple RecyclerViews.
     *
     * @param recyclerView The RecyclerView instance which started observing this adapter.
     *
     * @see [onDetachedFromRecyclerView]
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        when (val layoutManager = recyclerView.layoutManager) {
            is GridLayoutManager -> setLayoutSpanSize(layoutManager)
        }
    }

    /**
     * Calls the the recycler view holder to perform view binding,
     * and selection mode decoration if [ISupportActionMode] is active
     *
     * @see [SupportViewHolder.invoke]
     */
    override fun onBindViewHolder(holder: SupportViewHolder<T>, position: Int) {
        bindViewHolderByType(holder, position)
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method
     * should update the contents of the [androidx.recyclerview.widget.RecyclerView.ViewHolder.itemView]
     * to reflect the item at the given position.
     *
     * Note that unlike [android.widget.ListView], RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the `position` parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use [androidx.recyclerview.widget.RecyclerView.ViewHolder.getAdapterPosition]
     * which will have the updated adapter position.
     *
     * Partial bind vs full bind:
     *
     * The payloads parameter is a merge list from [.notifyItemChanged] or
     * [.notifyItemRangeChanged].  If the payloads list is not empty,
     * the ViewHolder is currently bound to old data and Adapter may run an efficient partial
     * update using the payload info.  If the payload is empty,  Adapter must run a full bind.
     * Adapter should not assume that the payload passed in notify methods will be received by
     * onBindViewHolder().  For example when the view is not attached to the screen, the
     * payload in notifyItemChange() will be simply dropped.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     * @param payloads A non-null list of merged payloads. Can be empty list if requires full
     * update.
     */
    override fun onBindViewHolder(
        holder: SupportViewHolder<T>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            onBindViewHolder(holder, position)
        else
            bindViewHolderByType(holder, position)
    }

    /**
     * Calls the the recycler view holder impl to perform view recycling
     *
     * @see [SupportViewHolder.onViewRecycled]
     */
    override fun onViewRecycled(holder: SupportViewHolder<T>) {
        with (holder) {
            supportActionMode = null
            onViewRecycled()
        }
    }

    /**
     * Return the view type of the item at `position` for the purposes
     * of view recycling.
     *
     *
     * The default implementation of this method returns 0, making the assumption of
     * a single view type for the adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     * `position`. Type codes need not be contiguous.
     */
    @LayoutRes
    override fun getItemViewType(position: Int): Int {
        if (hasExtraRow() && position == itemCount - 1)
            return when (networkState) {
                is NetworkState.Error ->
                    R.layout.support_layout_state_footer_error
                is NetworkState.Loading ->
                    R.layout.support_layout_state_footer_loading
                else -> super.getItemViewType(position)
            }

        return super.getItemViewType(position)
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return getCurrentList().size + if (hasExtraRow()) 1 else 0
    }

    /**
     * Returns a boolean indicating whether or not the adapter had data, and caters for [hasExtraRow]
     *
     * @return [Boolean]
     * @see hasExtraRow
     */
    override fun isEmpty(): Boolean {
        if (hasExtraRow())
            return itemCount < 2
        return itemCount < 1
    }

    /**
     * Returns a boolean to instruct the [GridLayoutManager] if an item at the position should
     * use a span size count of 1 otherwise defaults to the intended size
     *
     * @param position recycler position being rendered
     * @see setLayoutSpanSize
     */
    override fun isFullSpanItem(position: Int): Boolean =
        getItemViewType(position) == R.layout.support_layout_state_footer_loading ||
        getItemViewType(position) == R.layout.support_layout_state_footer_error

    /**
     * Informs view adapter of changes related to it's view holder
     */
    override fun updateSelection() {
        notifyDataSetChanged()
    }

    /**
     * Binds content view holder
     */
    override fun bindContentViewHolder(holder: SupportViewHolder<T>, position: Int) {
        runCatching {
            animateViewHolder(holder, position)
            val model = getItem(position)
            with(holder) {
                supportActionMode = supportAction
                invoke(model)
                onBindSelectionState(model)
            }
        }.exceptionOrNull()?.also {
            it.printStackTrace()
            Timber.tag(moduleTag).e(it)
        }
    }

    /**
     * Binds view holder by view type at [position]
     */
    override fun bindViewHolderByType(holder: SupportViewHolder<T>, position: Int) {
        when (getItemViewType(position)) {
            R.layout.support_layout_state_footer_loading ->
                holder(null)
            R.layout.support_layout_state_footer_error ->
                holder(null)
            else -> bindContentViewHolder(holder, position)
        }
    }

    /**
     * Returns a model at the given index
     */
    fun getItem(position: Int): T? {
        val currentList = getCurrentList()
        if (position <= RecyclerView.NO_POSITION || position >= currentList.size) {
            return null
        }

        if (position >= itemCount) {
            Timber.tag(moduleTag).w("Requesting out of bounds index at position: $position")
            return null
        }

        return currentList[position]
    }

    /**
     * Returns the List currently being displayed by the Adapter.
     *
     * This is not necessarily the most recent list passed to [submitList],
     * because a diff is computed asynchronously between the new list and the current list before
     * updating the currentList value.
     *
     * @return The list currently being displayed.
     */
    fun getCurrentList(): List<T> {
        return mDiffer.currentList
    }

    /**
     * Set the new list to be displayed.
     *
     * If a list is already being displayed, a diff will be computed on a background thread, which
     * will dispatch Adapter.notifyItem events on the main thread.
     *
     * @param list The new list to be displayed.
     */
    fun submitList(list: List<T>?, commitCallback: Runnable? = null) {
        mDiffer.submitList(list, commitCallback)
    }
}