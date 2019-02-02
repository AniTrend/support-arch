package io.wax911.support.custom.action.decorator

import android.widget.CheckBox
import androidx.cardview.widget.CardView
import io.wax911.support.R
import io.wax911.support.custom.recycler.holder.SupportViewHolder
import io.wax911.support.getColorFromAttr
import io.wax911.support.getCompatColor

/**
 * Action mode selection item decorator class
 */
interface SelectionDecorator<T> {

    /**
     * Applies view holder which can be used to check what the [SupportViewHolder.itemView] type is
     * and apply decorations on the selected or deselected item.
     *
     * This interface has a default implementation which can be overridden
     *
     * @param viewHolder the current view holder class which has been selected or deselected
     * @param isSelected state of hte current item
     */
    fun setBackgroundColor(viewHolder: SupportViewHolder<T>, isSelected: Boolean) {
        when {
            isSelected -> when {
                viewHolder.itemView is CardView -> viewHolder.itemView
                        .setCardBackgroundColor(
                                viewHolder.context.getCompatColor(R.color.colorTextGrey2nd)
                        )
                viewHolder.itemView is CheckBox -> viewHolder.itemView.isChecked = true
                else -> viewHolder.itemView.setBackgroundResource(R.drawable.selection_frame)
            }
            else -> when {
                viewHolder.itemView is CardView -> viewHolder.itemView
                        .setCardBackgroundColor(
                                viewHolder.context.getColorFromAttr(R.attr.cardColor)
                        )
                viewHolder.itemView is CheckBox -> viewHolder.itemView.isChecked = false
                else -> viewHolder.itemView.setBackgroundResource(0)
            }
        }
    }
}