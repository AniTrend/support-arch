package io.wax911.support.ui.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.LayoutRes
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.wax911.support.core.animator.ScaleAnimator
import io.wax911.support.core.animator.contract.SupportAnimator
import io.wax911.support.core.presenter.SupportPresenter
import io.wax911.support.data.model.NetworkState
import io.wax911.support.data.model.contract.SupportStateType
import io.wax911.support.extension.getLayoutInflater
import io.wax911.support.ui.R
import io.wax911.support.ui.action.contract.ISupportActionMode
import io.wax911.support.ui.recycler.holder.SupportViewHolder
import io.wax911.support.ui.recycler.holder.event.ItemClickListener
import java.util.*
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * Created by max on 2017/06/09.
 * Recycler view adapter implementation
 */

abstract class SupportViewAdapter<T>(
    protected val presenter: SupportPresenter<*>,
    private val  itemClickListener: ItemClickListener<T>,
    itemCallback: DiffUtil.ItemCallback<T> = getDefaultDiffItemCallback()
) : PagedListAdapter<T, SupportViewHolder<T>>(itemCallback), Filterable {

    var supportAction: ISupportActionMode<T>? = null
        set(value) {
            field = value?.apply {
                setRecyclerViewAdapter(this@SupportViewAdapter)
            }
        }

    var networkState: NetworkState? = null
        set(value) {
            val previousState = field
            val hadExtraRow = hasExtraRow()
            field = value
            val hasExtraRow = hasExtraRow()
            when {
                hadExtraRow != hasExtraRow -> {
                    if (hadExtraRow)
                        notifyItemRemoved(super.getItemCount())
                    else
                        notifyItemInserted(super.getItemCount())
                }
                hasExtraRow && previousState != field ->
                    notifyItemChanged(itemCount - 1)
            }
        }

    private var lastPosition: Int = 0

    /**
     * Invokes a filter to search for data on the current adapter
     */
    var searchQuery: String? by Delegates.observable(null) {
            _: KProperty<*>, _: String?, _: String? -> applyFilterIfRequired()
    }

    /**
     * Get currently set animation type for recycler view holder items,
     * if no custom animation is set @[ScaleAnimator]
     * will be assigned in [onAttachedToRecyclerView]
     *
     * @see [SupportAnimator]
     */
    private var customSupportAnimator: SupportAnimator? = null
        get() {
            if (field == null)
                field = ScaleAnimator()
            return field
        }

    /**
     * Return the stable ID for the item at <code>position</code>. If [hasStableIds]
     * would return false this method should return [RecyclerView.NO_ID].
     *
     * The default implementation of this method returns [RecyclerView.NO_ID].
     *
     * @param position Adapter position to query
     * @return the stable ID of the item at position
     */
    override fun getItemId(position: Int): Long {
        return when (!hasStableIds()) {
            true -> super.getItemId(position)
            else -> getItem(position)?.hashCode()?.toLong() ?: 0
        }
    }

    /**
     * Should provide the required view holder, this function is a substitute for [onCreateViewHolder] which now
     * has extended functionality
     */
    abstract fun createDefaultViewHolder(parent: ViewGroup, @LayoutRes viewType: Int, layoutInflater: LayoutInflater): SupportViewHolder<T>

    /**
     * Overridden implementation creates a default loading footer view when the [viewType] is
     * [R.layout.support_layout_state_footer] otherwise [createDefaultViewHolder] is called to
     * resolve the view holder type
     */
    override fun onCreateViewHolder(parent: ViewGroup, @LayoutRes viewType: Int): SupportViewHolder<T> {
        val layoutInflater = parent.context.getLayoutInflater()
        return when (viewType) {
            R.layout.support_layout_state_footer -> {
                return SupportFooterViewHolder(layoutInflater.inflate(
                    R.layout.support_layout_state_footer, parent, false)
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
     * @see [SupportViewHolder.onBindViewHolder]
     */
    override fun onBindViewHolder(holder: SupportViewHolder<T>, position: Int) {
        when (getItemViewType(position)) {
            R.layout.support_layout_state_footer ->
                holder.onBindViewHolder(null)
            else ->
                holder.onBindViewHolder(getItem(position))
        }
    }

    /**
     * Calls the the recycler view holder to perform view binding,
     * and selection mode decoration if [ISupportActionMode] is active
     *
     * @see [SupportViewHolder.onBindViewHolder]
     */
    override fun onBindViewHolder(holder: SupportViewHolder<T>, position: Int, payloads: MutableList<Any>) {
        when {
            payloads.isNotEmpty() -> {
                animateViewHolder(holder, position)
                val model = getItem(position)
                holder.apply {
                    clickListener = itemClickListener
                    supportActionMode = supportAction
                    onBindViewHolder(model)
                    onBindSelectionState(model)
                }
            }
            else -> onBindViewHolder(holder, position)
        }
    }

    /**
     * Calls the the recycler view holder impl to perform view recycling
     *
     * @see [SupportViewHolder.onViewRecycled]
     */
    override fun onViewRecycled(holder: SupportViewHolder<T>) {
        holder.apply {
            clickListener = null
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
            return R.layout.support_layout_state_footer
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun isEmpty(): Boolean {
        if (hasExtraRow())
            return itemCount < 2
        return itemCount < 1
    }
    /**
     * Returns a filter that can be used to constrain data with a filtering
     * pattern.
     *
     * This method is usually implemented by [android.widget.Adapter]
     * classes.
     *
     * @return a filter used to constrain data
     */
    override fun getFilter(): Filter = object : Filter() {

        /**
         *
         * Invoked in a worker thread to filter the data according to the
         * constraint. Subclasses must implement this method to perform the
         * filtering operation. Results computed by the filtering operation
         * must be returned as a [android.widget.Filter.FilterResults] that
         * will then be published in the UI thread through
         * [.publishResult].
         *
         *
         * **Contract:** When the constraint is null, the original
         * data must be restored.
         *
         * @param constraint the constraint used to filter the data
         * @return the results of the filtering operation
         *
         * @see .filter
         * @see .publishResult
         * @see android.widget.Filter.FilterResults
         */
        override fun performFiltering(constraint: CharSequence?) = FilterResults().apply {
            values = Collections.EMPTY_LIST
        }

        /**
         * Invoked in the UI thread to publish the filtering results in the
         * user interface. Subclasses must implement this method to display the
         * results computed in [.performFiltering].
         *
         * @param constraint the constraint used to filter the data
         * @param results the results of the filtering operation
         *
         * @see .filter
         * @see .performFiltering
         * @see android.widget.Filter.FilterResults
         */
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

        }
    }

    private fun hasExtraRow() = networkState?.status != SupportStateType.CONTENT

    protected fun isFullSpanItem(position: Int): Boolean =
        getItemViewType(position) == R.layout.support_layout_state_footer

    /**
     * Initial implementation is only specific for group types of recyclers,
     * in order to customize this an override is required.
     *
     * @param layoutManager grid layout manage for your recycler
     */
    private fun setLayoutSpanSize(layoutManager: GridLayoutManager) {
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int = when {
                isFullSpanItem(position) -> 1
                else -> layoutManager.spanCount
            }
        }
    }

    /**
     * Initial implementation is only specific for group types of recyclers,
     * in order to customize this an override is required.
     *
     * @param layoutParams StaggeredGridLayoutManager.LayoutParams for your recycler
     */
    private fun setLayoutSpanSize(layoutParams: StaggeredGridLayoutManager.LayoutParams, position: Int) {
        if (isFullSpanItem(position))
            layoutParams.isFullSpan = true
    }

    private fun animateViewHolder(holder: SupportViewHolder<T>?, position: Int) {
        holder?.apply {
            when (position > lastPosition) {
                true -> customSupportAnimator?.also { supportAnimator ->
                    supportAnimator.getAnimators(itemView).forEach { animator ->
                        with(animator) {
                            duration = supportAnimator.getAnimationDuration().toLong()
                            interpolator = supportAnimator.interpolator
                            start()
                        }
                    }
                }
            }
            lastPosition = position
        }
    }

    /**
     * Applies a recycler filter if [searchQuery] is not null or empty
     */
    fun applyFilterIfRequired() {
        if (!searchQuery.isNullOrBlank())
            filter.filter(searchQuery)
    }

    companion object {
        /**
         * Provides default behaviour for item callback to compare objects
         */
        fun <T> getDefaultDiffItemCallback(): DiffUtil.ItemCallback<T> {
            return object : DiffUtil.ItemCallback<T>() {
                /**
                 * Called to check whether two objects represent the same item.
                 *
                 *
                 * For example, if your items have unique ids, this method should check their id equality.
                 *
                 *
                 * Note: `null` items in the list are assumed to be the same as another `null`
                 * item and are assumed to not be the same as a non-`null` item. This callback will
                 * not be invoked for either of those cases.
                 *
                 * @param oldItem The item in the old list.
                 * @param newItem The item in the new list.
                 * @return True if the two items represent the same object or false if they are different.
                 *
                 * @see Callback.areItemsTheSame
                 */
                override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
                    return oldItem?.equals(newItem) ?: false
                }

                /**
                 * Called to check whether two items have the same data.
                 *
                 *
                 * This information is used to detect if the contents of an item have changed.
                 *
                 *
                 * This method to check equality instead of [Object.equals] so that you can
                 * change its behavior depending on your UI.
                 *
                 *
                 * For example, if you are using DiffUtil with a
                 * [RecyclerView.Adapter], you should
                 * return whether the items' visual representations are the same.
                 *
                 *
                 * This method is called only if [.areItemsTheSame] returns `true` for
                 * these items.
                 *
                 *
                 * Note: Two `null` items are assumed to represent the same contents. This callback
                 * will not be invoked for this case.
                 *
                 * @param oldItem The item in the old list.
                 * @param newItem The item in the new list.
                 * @return True if the contents of the items are the same or false if they are different.
                 *
                 * @see Callback.areContentsTheSame
                 */
                override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            }
        }
    }
}
