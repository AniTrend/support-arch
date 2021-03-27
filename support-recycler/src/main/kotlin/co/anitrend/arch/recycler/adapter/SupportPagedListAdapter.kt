package co.anitrend.arch.recycler.adapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import co.anitrend.arch.extension.ext.getLayoutInflater
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter
import co.anitrend.arch.recycler.adapter.controller.SupportAdapterController
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.arch.recycler.model.contract.IRecyclerItem
import co.anitrend.arch.recycler.model.contract.IRecyclerItemSpan
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

/**
 * Core implementation for handling complex logic for [androidx.paging.PagedListAdapter] and
 * [androidx.recyclerview.widget.RecyclerView.ViewHolder] binding logic
 *
 * @param differCallback Callback for calculating the diff between two non-null items in a list.
 * @param supportsStableIds Sets [PagedListAdapter.setHasStableIds] which is defaulted to true
 *
 * @see SupportViewHolder
 * @since v1.2.0
 */
abstract class SupportPagedListAdapter<T>(
    differCallback: DiffUtil.ItemCallback<T>,
    supportsStableIds: Boolean = false
) : SupportAdapter<T>, PagedListAdapter<T, SupportViewHolder>(differCallback) {

    init {
        this.setHasStableIds(supportsStableIds)
    }

    override var lastAnimatedPosition: Int = 0

    override val controller by lazy {
        SupportAdapterController(this)
    }

    override val clickableFlow = MutableStateFlow<ClickableItem>(ClickableItem.None)

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
                Timber.v(it, "getItemId(position: Int) -> position: $position")
                RecyclerView.NO_ID
            }
            else -> super.getItemId(position)
        }
    }

    /**
     * Overridden implementation [createDefaultViewHolder] calls to resolve the view holder type
     */
    override fun onCreateViewHolder(parent: ViewGroup, @LayoutRes viewType: Int): SupportViewHolder {
        val layoutInflater = parent.context.getLayoutInflater()
        return createDefaultViewHolder(parent, viewType, layoutInflater)
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
     * <p>Becoming detached from the window is not necessarily a permanent condition;
     * the consumer of an Adapter's views may choose to cache views offscreen while they
     * are not visible, attaching and detaching them as appropriate.</p>
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
        return ISupportAdapter.DEFAULT_VIEW_TYPE
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
            val data = requireNotNull(getItem(position)) {
                "getSpanSizeForItemAt(position: $position, spanCount: $spanCount) -> getItem(..) was null"
            }
            val spanSize = spanCount ?: IRecyclerItemSpan.INVALID_SPAN_COUNT
            mapper(data).getSpanSize(spanSize, position, resources)
        }.getOrElse {
            Timber.w(it, "Span size: $spanCount")
            // we don't know which span size to use so we use the supplied or default full size
            spanCount ?: ISupportAdapter.FULL_SPAN_SIZE
        }
    }

    /**
     * Informs view adapter of changes related to it's view holder
     */
    override fun notifyDataSetNeedsRefreshing() {
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
        runCatching {
            val data = requireNotNull(getItem(position))
            val recyclerItem: IRecyclerItem = mapper(data)
            holder.bind(position, payloads, recyclerItem, clickableFlow, supportAction)
            animateViewHolder(holder, position)
        }.onFailure { throwable ->
            Timber.w(throwable, "bindViewHolderByType(holder: .., position: .., payloads: ..)")
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
        clickableFlow.value = ClickableItem.None
    }
}