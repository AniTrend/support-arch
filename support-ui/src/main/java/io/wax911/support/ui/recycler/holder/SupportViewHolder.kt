package io.wax911.support.ui.recycler.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.wax911.support.ui.action.contract.ISupportActionMode
import io.wax911.support.ui.recycler.holder.event.ItemClickListener

/**
 * Created by max on 2017/06/09.
 * Recycler view holder implementation
 */

abstract class SupportViewHolder<T>(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {

    var clickListener: ItemClickListener<T>? = null
    var supportActionMode: ISupportActionMode<T>? = null

    /**
     * Constructs an int pair container with a boolean representing a valid adapter position
     * @return IntPair
     */
    private fun isValidIndexPair(): Pair<Int, Boolean> =
            Pair(adapterPosition, adapterPosition != RecyclerView.NO_POSITION)

    /**
     * Load image, text, buttons, etc. in this method from the given parameter
     *
     * @param model Is the liveData at the current adapter position
     */
    abstract fun onBindViewHolder(model: T?)

    /**
     * If any image views are used within the view holder, clear any pending async img requests
     * by using Glide.clear(ImageView) or Glide.with(context).clear(view) if using Glide v4.0
     */
    abstract fun onViewRecycled()

    /**
     * Handle any onclick events from our views
     *
     * @param v the view that has been clicked
     * @see View.OnClickListener
     */
    abstract override fun onClick(v: View)

    /**
     * Called when a view has been clicked and held.
     *
     * @param v The view that was clicked and held.
     *
     * @return true if the callback consumed the long click, false otherwise.
     */
    override fun onLongClick(v: View?): Boolean = false

    /**
     * Applying selection styling on the desired item
     * @param model the current liveData item
     */
    fun onBindSelectionState(model: T?) = supportActionMode?.apply {
        getSelectionDecorator().setBackgroundColor(this@SupportViewHolder,
                getAllSelectedItems().contains(model))
    }

    /**
     * Handle any onclick events from our views
     *
     * @param v the view that has been clicked
     * @see View.OnClickListener
     */
    protected fun performClick(data: T?, v: View) {
        val pair = isValidIndexPair()
        if (pair.second && isClickable(data))
            clickListener?.onItemClick(v, Pair(pair.first, data))
    }

    /**
     * Called when a view has been clicked and held.
     *
     * @param v The view that was clicked and held.
     * @return true if the supportActionMode consumed the long click, false otherwise.
     */
    protected fun performLongClick(data: T?, v: View): Boolean {
        val pair = isValidIndexPair()
        return when (pair.second && isLongClickable(data)) {
            true -> { clickListener?.onItemLongClick(v, Pair(pair.first, data)); true }
            else -> false
        }
    }

    private fun isClickable(clicked: T?): Boolean =
            supportActionMode?.isSelectionClickable(this, clicked) ?: true



    private fun isLongClickable(clicked: T?): Boolean =
            supportActionMode?.isLongSelectionClickable(this, clicked) ?: true
}
