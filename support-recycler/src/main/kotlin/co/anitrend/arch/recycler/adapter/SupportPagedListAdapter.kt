package co.anitrend.arch.recycler.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.getLayoutInflater
import co.anitrend.arch.recycler.R
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter.Companion.getDefaultDiffItemCallback
import co.anitrend.arch.recycler.common.ItemClickListener
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.arch.recycler.model.RecyclerItem
import co.anitrend.arch.recycler.shared.SupportFooterErrorItem
import co.anitrend.arch.recycler.shared.SupportFooterLoadingItem
import timber.log.Timber

/**
 * Core implementation for handling complex logic for [androidx.paging.PagedListAdapter] and
 * [androidx.recyclerview.widget.RecyclerView.ViewHolder] binding logic
 *
 * @see SupportViewHolder
 * @since v1.2.0
 */
abstract class SupportPagedListAdapter<H : SupportViewHolder>(
    itemCallback: DiffUtil.ItemCallback<RecyclerItem<H>> = getDefaultDiffItemCallback(),
    protected val clickListener: ItemClickListener<RecyclerItem<*>>,
    protected val context: Context
) : ISupportAdapter<RecyclerItem<H>, H>, PagedListAdapter<RecyclerItem<H>, H>(itemCallback) {

    override val moduleTag: String = javaClass.name

    override var lastAnimatedPosition: Int = 0

    /**
     * Network state which will be used by [SupportFooterErrorItem]
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
    override fun onCreateViewHolder(parent: ViewGroup, @LayoutRes viewType: Int): H {
        val layoutInflater = parent.context.getLayoutInflater()
        return when (viewType) {
            R.layout.support_layout_state_footer_loading -> {
                val item = SupportFooterLoadingItem(viewType, stateConfiguration)
                val view = layoutInflater.inflate(item.layout, parent, false)
                return item.createViewHolder(view) as H
            }
            R.layout.support_layout_state_footer_error -> {
                val item = SupportFooterErrorItem(viewType, retryFooterAction, networkState, stateConfiguration)
                val view = layoutInflater.inflate(item.layout, parent, false)
                return item.createViewHolder(view) as H
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
    override fun onViewAttachedToWindow(holder: H) {
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
    override fun onViewDetachedFromWindow(holder: H) {
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
     * and selection mode decorations are set up
     *
     * @see [SupportViewHolder.bind]
     */
    override fun onBindViewHolder(holder: H, position: Int) {
        bindViewHolderByType(holder, position)
    }

    /**
     * Calls the the recycler view holder to perform view binding,
     * and selection mode decorations are set up
     *
     * @see [SupportViewHolder.bind]
     */
    override fun onBindViewHolder(
        holder: H,
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
    override fun onViewRecycled(holder: H) {
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

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    /**
     * Returns a boolean to instruct the [GridLayoutManager] if an item at the position should
     * use a span size count of 1 otherwise defaults to the intended size
     *
     * @param position recycler position being rendered
     * @param spanCount current size of the span count from the layout manager
     *
     * @see setLayoutSpanSize
     */
    override fun isFullSpanItem(position: Int, spanCount: Int): Boolean {
        val item = getItem(position)
        val spanSize = item?.getSpanSize(0, position, context.resources)
        return spanSize == ISupportAdapter.FULL_SPAN_SIZE
    }

    /**
     * Informs view adapter of changes related to it's view holder
     */
    override fun updateSelection() {
        notifyDataSetChanged()
    }

    /**
     * Binds content view holder
     */
    override fun bindContentViewHolder(
        holder: H,
        position: Int,
        payloads: List<Any>
    ) {
        runCatching {
            animateViewHolder(holder, position)
            val model = getItem(position)
            model?.bind(holder, position, payloads, clickListener)
            model?.also { m ->
                // TODO: trigger decoration based on the current item
                if (m.id != RecyclerView.NO_ID)
                    supportAction?.containsItem(m.id)
            }
        }.exceptionOrNull()?.also {
            it.printStackTrace()
            Timber.tag(moduleTag).e(it)
        }
    }

    /**
     * Binds view holder by view type at [position]
     */
    override fun bindViewHolderByType(
        holder: H,
        position: Int,
        payloads: List<Any>
    ) {
        when (getItemViewType(position)) {
            R.layout.support_layout_state_footer_loading ->
                holder.bind(null, null)
            R.layout.support_layout_state_footer_error ->
                holder.bind(null, null)
            else -> bindContentViewHolder(holder, position, payloads)
        }
    }
}