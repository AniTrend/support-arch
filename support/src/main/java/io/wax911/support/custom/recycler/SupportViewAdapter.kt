package io.wax911.support.custom.recycler

import android.content.Context
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.wax911.support.base.animation.AnimationBase
import io.wax911.support.base.event.ItemClickListener
import io.wax911.support.base.event.RecyclerChangeListener
import io.wax911.support.custom.animation.ScaleAnimation
import io.wax911.support.custom.presenter.SupportPresenter
import io.wax911.support.isEmptyOrNull
import io.wax911.support.isLowRamDevice
import io.wax911.support.replaceWith
import io.wax911.support.util.SupportActionUtil

/**
 * Created by max on 2017/06/09.
 * Recycler view adapter implementation
 */

abstract class SupportViewAdapter<T>(private val context: Context) : RecyclerView.Adapter<SupportViewHolder<T>>(), Filterable, RecyclerChangeListener<T> {

    lateinit var presenter: SupportPresenter<*>
    lateinit var clickListener: ItemClickListener<T>

    var supportAction: SupportActionUtil<T>? = null
        set(value) {
            field = value
            field!!.recyclerAdapter = this
        }

    private var lastPosition: Int = 0
    private val isLowRamDevice: Boolean by lazy { context.isLowRamDevice() }

    /**
     * Get currently set animation type for recycler view holder items,
     * if no custom animation is set @[ScaleAnimation]
     * will be assigned in [.onAttachedToRecyclerView]
     * <br></br>
     *
     * @see AnimationBase
     */
    private var customAnimation: AnimationBase? = null
        get() = when (field == null) {
            true -> {
                field = ScaleAnimation()
                field
            }
            false -> field
        }

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
            holder.clickListener = clickListener
            holder.supportActionUtil = supportAction
            holder.onBindViewHolder(model)
            holder.onBindSelectionState(model)
        }
    }

    /**
     * Calls the the recycler view holder impl to perform view recycling
     * @see SupportViewHolder
     * <br></br><br></br>
     * default implemation is already done for you
     */
    override fun onViewRecycled(holder: SupportViewHolder<T>) {
        holder.onViewRecycled()
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * <br></br>
     * The default method has already been implemented for you.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * Clears data sets and notifies the recycler observer about the changed data set
     */
    fun clearDataSet() {
        data.clear()
        if (clone != null)
            clone = ArrayList()
        notifyDataSetChanged()
    }

    fun hasData() = !data.isEmptyOrNull()

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

    protected fun isRecyclerStateType(viewType: Int): Boolean =
            viewType == 1

    protected fun isFullSpanItem(position: Int): Boolean =
            position == 0

    private fun animateViewHolder(holder: SupportViewHolder<T>?, position: Int) {
        when (!isLowRamDevice && position > lastPosition) {
            true -> when (holder != null && customAnimation != null) {
                true -> for (animator in customAnimation!!.getAnimators(holder!!.itemView)) {
                    animator.duration = customAnimation!!.getAnimationDuration().toLong()
                    animator.interpolator = customAnimation!!.getInterpolator()
                    animator.start()
                }
            }
        }
        lastPosition = position
    }
}
