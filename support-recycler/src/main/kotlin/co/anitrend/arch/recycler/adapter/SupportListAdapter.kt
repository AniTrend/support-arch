package co.anitrend.arch.recycler.adapter

import android.content.res.Resources
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.*
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.arch.extension.ext.getLayoutInflater
import co.anitrend.arch.recycler.R
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.arch.recycler.model.contract.IRecyclerItem
import co.anitrend.arch.recycler.model.contract.IRecyclerItemSpan
import co.anitrend.arch.recycler.shared.SupportErrorItem
import co.anitrend.arch.recycler.shared.SupportLoadingItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

/**
 * Core implementation for handling complex logic for [List]s and
 * [androidx.recyclerview.widget.RecyclerView.ViewHolder] binding logic
 *
 * @param differCallback Callback for calculating the diff between two non-null items in a list.
 * @param supportsStableIds Sets [PagedListAdapter.setHasStableIds] which is defaulted to true
 *
 * @since v1.2.0
 */
abstract class SupportListAdapter<T>(
    differCallback: DiffUtil.ItemCallback<T>,
    supportsStableIds: Boolean = false
) : ISupportAdapter<T>, RecyclerView.Adapter<SupportViewHolder>() {

    init {
        this.setHasStableIds(supportsStableIds)
    }

    protected abstract val resources: Resources

    private val mDiffer: AsyncListDiffer<T> by lazy(UNSAFE) {
        AsyncListDiffer(this, differCallback)
    }

    override val moduleTag: String = javaClass.simpleName

    override var lastAnimatedPosition: Int = 0

    /**
     * Dispatches clicks from parent views
     */
    protected val clickableItemMutableStateFlow =
        MutableStateFlow<ClickableItem?>(null)

    override val clickableStateFlow: StateFlow<ClickableItem?> = clickableItemMutableStateFlow

    /**
     * Network state which will be used by [SupportErrorItem]
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
                hasExtraRow && previousState != value ->
                    notifyItemChanged(itemCount - 1)
            }
        }

    /**
     * Returns a model at the given index
     */
    open fun getItem(position: Int): T? {
        val currentList = getCurrentList()
        if (!isWithinIndexBounds(position))
            return null

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
    open fun getCurrentList(): List<T> {
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
    open fun submitList(list: List<T>?, commitCallback: Runnable? = null) {
        mDiffer.submitList(list, commitCallback)
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
            true -> runCatching {
                getStableIdFor(getItem(position))
            }.getOrElse {
                Timber.tag(moduleTag).v(it, "getItemId(position: Int) -> position: $position")
                RecyclerView.NO_ID
            }
            else -> super.getItemId(position)
        }
    }

    /**
     * Overridden implementation creates a default loading footer view when the [viewType] is
     * [R.layout.support_layout_state_footer_loading] or [R.layout.support_layout_state_footer_error]
     * otherwise [createDefaultViewHolder] is called to resolve the view holder type
     */
    override fun onCreateViewHolder(parent: ViewGroup, @LayoutRes viewType: Int): SupportViewHolder {
        val layoutInflater = parent.context.getLayoutInflater()
        return when (viewType) {
            R.layout.support_layout_state_footer_loading -> {
                SupportLoadingItem.createViewHolder(parent, layoutInflater).also {
                    val model = SupportLoadingItem(stateConfiguration)
                    it.bind(RecyclerView.NO_POSITION, emptyList(), model, clickableItemMutableStateFlow)
                }
            }
            R.layout.support_layout_state_footer_error -> {
                SupportErrorItem.createViewHolder(parent, layoutInflater).also {
                    val model = SupportErrorItem(networkState, stateConfiguration)
                    it.bind(RecyclerView.NO_POSITION, emptyList(), model, clickableItemMutableStateFlow)
                }
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
    override fun onViewAttachedToWindow(holder: SupportViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.layoutParams?.also {
            when (it is StaggeredGridLayoutManager.LayoutParams) {
                true -> setLayoutSpanSize(it, holder.absoluteAdapterPosition)
            }
        }
    }

    /**
     * Called when a view created by this adapter has been detached from its window.
     *
     * Becoming detached from the window is not necessarily a permanent condition;
     * the consumer of an Adapter's views may choose to cache views offscreen while they
     * are not visible, attaching and detaching them as appropriate.
     *
     * @param holder Holder of the view being detached
     */
    override fun onViewDetachedFromWindow(holder: SupportViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
        // disable view holder recycling on detached from window
        // holder.onViewRecycled()
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
     * and selection mode decorations are set up
     *
     * @see [SupportViewHolder.bind]
     */
    override fun onBindViewHolder(holder: SupportViewHolder, position: Int) {
        bindViewHolderByType(holder, position)
    }

    /**
     * Calls the the recycler view holder to perform view binding,
     * and selection mode decorations are set up
     *
     * @see [SupportViewHolder.bind]
     */
    override fun onBindViewHolder(
        holder: SupportViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            onBindViewHolder(holder, position)
        else
            bindViewHolderByType(holder, position, payloads)
    }

    /**
     * Calls the the recycler view holder impl to perform view recycling
     *
     * @see [SupportViewHolder.onViewRecycled]
     */
    override fun onViewRecycled(holder: SupportViewHolder) {
        holder.onViewRecycled()
    }

    /**
     * Return the view type of the item at [position] for the purposes of view recycling.
     *
     * The default implementation of this method returns 0, making the assumption of
     * a single view type for the adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     * [position]. Type codes need not be contiguous.
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
     * Fetches the non-nullable item of the underlying list with-in the adapter
     *
     * @param position Index of the item to get
     *
     * @throws IllegalStateException
     */
    @Throws(IllegalStateException::class)
    override fun requireItem(position: Int): T {
        return requireNotNull(getItem(position)) {
            "Required item at position: $position is null, constraint violated"
        }
    }

    /**
     * Informs us if the given [position] is within bounds of our underlying collection
     */
    override fun isWithinIndexBounds(position: Int): Boolean {
        val trueItemCount = itemCount.let { if (hasExtraRow()) it - 1 else it  }
        if (position <= RecyclerView.NO_POSITION || position >= trueItemCount) {
            Timber.tag(moduleTag).w("Invalid selected position: $position")
            return false
        }
        return true
    }

    /**
     * Should return the span size for the item at [position], when called from
     * [androidx.recyclerview.widget.GridLayoutManager] [spanCount] will be the span
     * size for the item at the [position].
     *
     * Otherwise if called from [androidx.recyclerview.widget.StaggeredGridLayoutManager]
     * then [spanCount] may be null
     *
     * @param position item position in the adapter
     * @param spanCount current span count for the layout manager
     *
     * @see co.anitrend.arch.recycler.model.contract.IRecyclerItemSpan
     */
    override fun getSpanSizeForItemAt(position: Int, spanCount: Int?): Int? {
        return runCatching {
            val item = mapper(requireItem(position))
            val spanSize = spanCount ?: IRecyclerItemSpan.INVALID_SPAN_COUNT
            item.getSpanSize(spanSize, position, resources)
        }.getOrElse {
            if (!hasExtraRow())
                Timber.tag(moduleTag).w(it)
            Timber.tag(moduleTag).v("Span size: $spanCount")
            // we don't know which span size to use so we use the supplied or default full size
            spanCount ?: ISupportAdapter.FULL_SPAN_SIZE
        }
    }

    override fun getItemCount(): Int {
        return getCurrentList().size + if (hasExtraRow()) 1 else 0
    }

    /**
     * Informs view adapter of changes related to it's view holder
     */
    override fun updateSelection() {
        notifyDataSetChanged()
    }

    /**
     * Binds view holder by view type at [position]
     */
    override fun bindViewHolderByType(
        holder: SupportViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        val recyclerItem: IRecyclerItem? = when (getItemViewType(position)) {
            R.layout.support_layout_state_footer_loading -> {
                SupportLoadingItem(stateConfiguration)
            }
            R.layout.support_layout_state_footer_error -> {
                SupportErrorItem(networkState, stateConfiguration)
            }
            else -> {
                runCatching {
                    mapper(requireItem(position))
                }.onFailure {
                    Timber.tag(moduleTag).v(it, "bindViewHolderByType(...)->when (getItemViewType(position))")
                }.getOrNull()
            }
        }

        runCatching {
            holder.bind(
                position,
                payloads,
                recyclerItem!!,
                clickableItemMutableStateFlow,
                supportAction
            )
            animateViewHolder(holder, position)
        }.onFailure {
            Timber.tag(moduleTag).w(it, "bindViewHolderByType(holder: SupportViewHolder, position: Int, payloads: List<Any>)")
        }
    }

    /**
     * Triggered when the lifecycleOwner reaches it's onPause state
     *
     * @see [androidx.lifecycle.LifecycleOwner]
     */
    override fun onPause() {
        super.onPause()
        // clear our state flow, when the lifecycle owner parent reaches its onPaused state
        clickableItemMutableStateFlow.value = null
    }
}