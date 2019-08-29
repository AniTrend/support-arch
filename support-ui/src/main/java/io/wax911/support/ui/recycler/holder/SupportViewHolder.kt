package io.wax911.support.ui.recycler.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.wax911.support.ui.action.contract.ISupportActionMode
import io.wax911.support.ui.recycler.holder.event.ItemClickListener


/**
 * Recycler view holder implementation
 *
 * @since v1.1.0
 */
abstract class SupportViewHolder<T>(
    view: View
) : RecyclerView.ViewHolder(view) {

    var supportActionMode: ISupportActionMode<T>? = null

    /**
     * Constructs an int pair container with a boolean representing a valid adapter position
     * @return IntPair
     */
    private fun isValidIndexPair(): Pair<Int, Boolean> =
            Pair(adapterPosition, adapterPosition != RecyclerView.NO_POSITION)

    /**
     * Load images, text, buttons, etc. in this method from the given parameter
     *
     * @param model Is the liveData at the current adapter position
     */
    abstract operator fun invoke(model: T?)

    /**
     * If any image views are used within the view holder, clear any pending async requests
     * by using [com.bumptech.glide.RequestManager.clear]
     *
     * @see com.bumptech.glide.Glide
     */
    abstract fun onViewRecycled()

    /**
     * Handle any onclick events from our views, optionally you can call
     * [performClick] to dispatch [Pair]<[Int], T> on the [ItemClickListener]
     *
     * @param view the view that has been clicked
     */
    abstract fun onItemClick(view: View)

    /**
     * Called when a view has been clicked and held. Optionally you can call
     * [performLongClick] to dispatch [Pair]<[Int], T> on the [ItemClickListener].
     *
     * If [ISupportActionMode] is then long clicking an items will start the section action mode
     *
     * @param view The view that was clicked and held.
     * @return [Boolean] true if the callback consumed the long click, false otherwise.
     */
    open fun onLongItemClick(view: View): Boolean = false

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
     * @param view the view that has been clicked
     * @see View.OnClickListener
     */
    protected fun performClick(entity: T?, view: View, clickListener: ItemClickListener<T>) {
        val pair = isValidIndexPair()
        if (pair.second && isClickable(entity))
            clickListener.onItemClick(
                view, Pair(pair.first, entity)
            )
    }

    /**
     * Called when a view has been clicked and held.
     *
     * @param view The view that was clicked and held.
     * @return true if the supportActionMode consumed the long click, false otherwise.
     */
    protected fun performLongClick(entity: T?, view: View, clickListener: ItemClickListener<T>): Boolean {
        val pair = isValidIndexPair()
        return when (pair.second && isLongClickable(entity)) {
            true -> {
                clickListener.onItemLongClick(
                    view, Pair(pair.first, entity)
                )
                true
            }
            else -> false
        }
    }

    private fun isClickable(clicked: T?): Boolean =
            supportActionMode?.isSelectionClickable(this, clicked) ?: true



    private fun isLongClickable(clicked: T?): Boolean =
            supportActionMode?.isLongSelectionClickable(this, clicked) ?: true
}
