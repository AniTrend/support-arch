package io.wax911.support.recycler.adapter

import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.wax911.support.animator.contract.SupportAnimator
import io.wax911.support.attribute.RecyclerType
import io.wax911.support.recycler.holder.event.ItemClickListener
import io.wax911.support.recycler.adapter.event.RecyclerChangeListener
import io.wax911.support.action.contract.ISupportActionMode
import io.wax911.support.animator.ScaleAnimator
import io.wax911.support.presenter.SupportPresenter
import io.wax911.support.recycler.holder.SupportViewHolder
import io.wax911.support.replaceWith
import java.util.*

/**
 * Created by max on 2017/06/09.
 * Recycler view adapter implementation
 */

abstract class SupportViewAdapter<T> : RecyclerView.Adapter<SupportViewHolder<T>>(), Filterable, RecyclerChangeListener<T> {

    lateinit var presenter: SupportPresenter<*>
    var itemClickListener: ItemClickListener<T>? = null

    var supportAction: ISupportActionMode<T>? = null
        set(value) {
            field = value?.apply {
                setRecyclerViewAdapter(this@SupportViewAdapter)
            }
        }

    private var lastPosition: Int = 0

    /**
     * Get currently set animation type for recycler view holder items,
     * if no custom animation is set @[ScaleAnimator]
     * will be assigned in [.onAttachedToRecyclerView]
     * <br></br>
     *
     * @see SupportAnimator
     */
    private var customSupportAnimator: SupportAnimator? = ScaleAnimator()

    protected val data: MutableList<T> by lazy { ArrayList<T>() }
    protected var clone: List<T>? = null

    override fun getItemId(position: Int): Long {
        return when (!hasStableIds()) {
            true -> super.getItemId(position)
            else -> return when(data[position] != null) {
                true -> data[position]!!.hashCode().toLong()
                false -> 0
            }
        }
    }

    override fun onItemsInserted(swap: List<T>) {
        data.replaceWith(swap)
        notifyDataSetChanged()
    }

    override fun onItemRangeInserted(swap: List<T>) {
        val startRange = itemCount
        val difference: Int
        data.addAll(swap)
        difference = itemCount - startRange
        if (difference > 5)
            notifyItemRangeInserted(startRange, difference)
        else if (difference != 0)
            notifyDataSetChanged()
    }

    override fun onItemRangeChanged(swap: List<T>) {
        val startRange = itemCount
        val difference = swap.size - startRange
        data.replaceWith(swap)
        notifyItemRangeChanged(startRange, difference)
    }


    override fun onItemChanged(swap: T, position: Int) {
        data[position] = swap
        notifyItemChanged(position)
    }

    override fun onItemRemoved(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupportViewHolder<T>

    override fun onViewAttachedToWindow(holder: SupportViewHolder<T>) {
        super.onViewAttachedToWindow(holder)
        if (holder.itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams)
            setLayoutSpanSize(holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams, holder.adapterPosition)
    }

    override fun onViewDetachedFromWindow(holder: SupportViewHolder<T>) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager)
            setLayoutSpanSize(layoutManager)
    }

    /**
     * Calls the the recycler view holder to perform view binding
     * @see SupportViewHolder
     * <br></br><br></br>
     * default implemation is already done for you
     */
    override fun onBindViewHolder(holder: SupportViewHolder<T>, position: Int) {
        if (itemCount > 0) {
            animateViewHolder(holder, position)
            val model = data[position]
            holder.apply {
                clickListener = itemClickListener
                supportActionMode = supportAction
                onBindViewHolder(model)
                onBindSelectionState(model)
            }
        }
    }

    /**
     * Calls the the recycler view holder impl to perform view recycling
     * @see SupportViewHolder
     */
    override fun onViewRecycled(holder: SupportViewHolder<T>) = holder.onViewRecycled()


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
        override fun performFiltering(constraint: CharSequence?): FilterResults? = FilterResults().also {
            it.values = Collections.EMPTY_LIST
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
            results?.also {
                data.clear()
                data += results.values as Collection<T>
                notifyDataSetChanged()
            }
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * <br>
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int = data.size

    /**
     * Clears data sets and notifies the recycler observer about the changed data set
     */
    fun clearDataSet() {
        data.clear()
        if (clone != null)
            clone = ArrayList()
        notifyDataSetChanged()
    }

    fun hasData() = !data.isNullOrEmpty()

    /**
     * Initial implementation is only specific for group types of recyclers,
     * in order to customize this an override is required.
     * <br></br>
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
     * <br></br>
     * @param layoutParams StaggeredGridLayoutManager.LayoutParams for your recycler
     */
    private fun setLayoutSpanSize(layoutParams: StaggeredGridLayoutManager.LayoutParams, position: Int) {
        if (isFullSpanItem(position))
            layoutParams.isFullSpan = true
    }

    protected fun isRecyclerStateType(viewType: Int): Boolean = when (viewType) {
        RecyclerType.RECYCLER_TYPE_EMPTY, RecyclerType.RECYCLER_TYPE_LOADING,
        RecyclerType.RECYCLER_TYPE_HEADER -> true
        else -> false
    }

    protected fun isFullSpanItem(position: Int): Boolean = false

    private fun animateViewHolder(holder: SupportViewHolder<T>?, position: Int) {
        holder?.also { h ->
            when (position > lastPosition) {
                true -> customSupportAnimator?.also { a ->
                    for (animator in a.getAnimators(h.itemView)) {
                        animator.duration = a.getAnimationDuration().toLong()
                        animator.interpolator = a.getInterpolator()
                        animator.start()
                    }
                }
            }
            lastPosition = position
        }
    }
}
