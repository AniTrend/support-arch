package io.wax911.support.custom.recycler

import android.content.Context
import android.view.View

import com.annimon.stream.IntPair
import androidx.recyclerview.widget.RecyclerView
import io.wax911.support.base.event.ItemClickListener
import io.wax911.support.util.SupportActionUtil

/**
 * Created by max on 2017/06/09.
 * Recycler view holder implementation
 */

abstract class SupportViewHolder<T>(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {

    var supportActionUtil: SupportActionUtil<T>? = null

    val context: Context by lazy { itemView.context.applicationContext }

    /**
     * Constructs an int pair container with a boolean representing a valid adapter position
     * @return IntPair
     */
    private val isValidIndexPair: IntPair<Boolean> =
            IntPair(adapterPosition, adapterPosition != RecyclerView.NO_POSITION)

    /**
     * Load image, text, buttons, etc. in this method from the given parameter
     * <br></br>
     *
     * @param model Is the mutableLiveData at the current adapter position
     */
    abstract fun onBindViewHolder(model: T)

    /**
     * If any image views are used within the view holder, clear any pending async img requests
     * by using Glide.clear(ImageView) or Glide.with(context).clear(view) if using Glide v4.0
     */
    abstract fun onViewRecycled()

    /**
     * Handle any onclick events from our views
     * <br></br>
     *
     * @param v the view that has been clicked
     * @see View.OnClickListener
     */
    abstract override fun onClick(v: View)

    /**
     * Applying selection styling on the desired item
     * @param model the current mutableLiveData item
     */
    fun onBindSelectionState(model: T) {
        when (supportActionUtil != null) {
            true -> supportActionUtil!!.setBackgroundColor(
                    this, supportActionUtil!!.selectedItems.contains(model)
            )
        }
    }

    /**
     * Handle any onclick events from our views
     * <br></br>
     *
     * @param v the view that has been clicked
     * @see View.OnClickListener
     */
    protected fun performClick(clickListener: ItemClickListener<T>, data: List<T>, v: View) {
        val pair = isValidIndexPair
        val model: T = data[pair.first]
        if (pair.second && isClickable(model))
            clickListener.onItemClick(v, IntPair(pair.first, model))
    }

    /**
     * Called when a view has been clicked and held.
     *
     * @param v The view that was clicked and held.
     * @return true if the supportActionUtil consumed the long click, false otherwise.
     */
    protected fun performLongClick(clickListener: ItemClickListener<T>, data: List<T>, v: View): Boolean {
        val pair = isValidIndexPair
        val model: T = data[pair.first]
        return when (pair.second && isLongClickable(model)) {
            true -> { clickListener.onItemLongClick(v, IntPair(pair.first, model))
                true
            }
            else -> false
        }
    }

    protected fun isClickable(clicked: T): Boolean = when (supportActionUtil != null) {
        true -> supportActionUtil!!.isSelectionClickable(this, clicked)
        false -> true
    }


    protected fun isLongClickable(clicked: T): Boolean = when (supportActionUtil != null) {
        true -> supportActionUtil!!.isLongSelectionClickable(this, clicked)
        false -> true
    }
}
