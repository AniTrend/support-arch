package co.anitrend.arch.ui.recycler.adapter

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import co.anitrend.arch.theme.animator.contract.ISupportAnimator
import co.anitrend.arch.core.presenter.SupportPresenter
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.getLayoutInflater
import co.anitrend.arch.ui.R
import co.anitrend.arch.ui.action.contract.ISupportActionMode
import co.anitrend.arch.ui.recycler.adapter.contract.ISupportViewAdapter
import co.anitrend.arch.ui.recycler.adapter.contract.ISupportViewAdapter.Companion.getDefaultDiffItemCallback
import co.anitrend.arch.ui.recycler.common.SupportFooterErrorViewHolder
import co.anitrend.arch.ui.recycler.common.SupportFooterLoadingViewHolder
import co.anitrend.arch.ui.recycler.holder.SupportViewHolder
import co.anitrend.arch.ui.util.SupportStateLayoutConfiguration
import timber.log.Timber

/**
 * Core implementation for handling complex logic for [androidx.paging.PagedListAdapter] and
 * [androidx.recyclerview.widget.RecyclerView.ViewHolder] binding logic
 *
 * @since v0.9.X
 * @see SupportViewHolder
 */
abstract class SupportPagedListAdapter<T>(
    protected val presenter: SupportPresenter<*>,
    itemCallback: DiffUtil.ItemCallback<T> = getDefaultDiffItemCallback()
) : ISupportViewAdapter<T>, PagedListAdapter<T, SupportViewHolder<T>>(itemCallback) {

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
     * Configuration for the state based footer
     */
    override lateinit var stateConfiguration: SupportStateLayoutConfiguration


    /**
     * Assigned if the current adapter supports needs to support [ISupportActionMode]
     */
    override var supportAction: ISupportActionMode<T>? = null
        set(value) {
            field = value?.apply {
                setRecyclerViewAdapter(this@SupportPagedListAdapter)
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
        when (getItemViewType(position)) {
            R.layout.support_layout_state_footer_loading ->
                holder(null)
            R.layout.support_layout_state_footer_error ->
                holder(null)
            else -> runCatching {
                holder(getItem(position))
            }.exceptionOrNull()?.also {
                it.printStackTrace()
                Timber.tag(moduleTag).e(it)
            }
        }
    }

    /**
     * Calls the the recycler view holder to perform view binding,
     * and selection mode decoration if [ISupportActionMode] is active
     *
     * @see [SupportViewHolder.invoke]
     */
    override fun onBindViewHolder(
        holder: SupportViewHolder<T>,
        position: Int, payloads:
        MutableList<Any>
    ) {
        animateViewHolder(holder, position)
        val model = getItem(position)
        with(holder) {
            supportActionMode = supportAction
            invoke(model)
            onBindSelectionState(model)
        }
        if (payloads.isEmpty())
            onBindViewHolder(holder, position)
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
}