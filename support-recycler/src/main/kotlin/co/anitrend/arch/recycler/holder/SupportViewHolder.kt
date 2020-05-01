package co.anitrend.arch.recycler.holder

import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.recycler.common.ItemClickListener
import co.anitrend.arch.recycler.model.RecyclerItem

open class SupportViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {

    private var item: RecyclerItem<*>? = null
    private var clickListener: ItemClickListener<RecyclerItem<*>>? = null

    open val onClickListener = OnClickListener { v ->
        val pair = isValidIndexPair()
        if (pair.second) {
            clickListener?.onItemClick(v, Pair(pair.first, item))
        }
    }

    open val onLongClickListener = OnLongClickListener { v ->
        val pair = isValidIndexPair()
        if (pair.second) {
            clickListener?.onItemLongClick(v, Pair(pair.first, item))
            true
        } else false
    }

    /**
     * Constructs an int pair container with a boolean representing a valid adapter position
     *
     * @return [Pair] of [Int] and [Boolean]
     */
    private fun isValidIndexPair(): Pair<Int, Boolean> =
        Pair(adapterPosition, adapterPosition != RecyclerView.NO_POSITION)

    /**
     * Load images, text, buttons, etc. in this method from the given parameter
     */
    open fun bind(
        item: RecyclerItem<*>?,
        listener: ItemClickListener<RecyclerItem<*>>?
    ) {
        if (item?.isClickable == true)
            itemView.setOnClickListener(onClickListener)
            clickListener = listener
        if (item?.isLongClickable == true)
            itemView.setOnLongClickListener(onLongClickListener)

        if (item?.isClickable == true || item?.isLongClickable == true)
            clickListener = listener
    }

    /**
     * Clear or unbind any references the views might be using, e.g. image loading
     * libraries, data binding, callbacks e.t.c
     */
    open fun onViewRecycled() {
        if (item?.isClickable == true)
            itemView.setOnClickListener(null)
        if (item?.isLongClickable == true)
            itemView.setOnLongClickListener(null)
        item = null
        clickListener = null
    }
}