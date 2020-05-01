package co.anitrend.arch.ui.action.decorator

import android.widget.CheckBox
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import co.anitrend.arch.extension.getColorFromAttr
import co.anitrend.arch.extension.getCompatColor
import co.anitrend.arch.theme.R
import co.anitrend.arch.ui.recycler.holder.SupportViewHolder

/**
 * A helper interface for applying decorations on sections for [co.anitrend.arch.ui.action.SupportActionMode]
 *
 * @since v0.9.X
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
     * @param drawableRes drawable overlay
     */
    fun setBackgroundColor(viewHolder: SupportViewHolder<T>, isSelected: Boolean, @DrawableRes drawableRes: Int = R.drawable.selection_frame) {
        when {
            isSelected -> when {
                viewHolder.itemView is CardView -> viewHolder.itemView
                    .setCardBackgroundColor(
                        viewHolder.itemView.context.getCompatColor(R.color.colorPrimaryDark)
                    )
                viewHolder.itemView is CheckBox -> viewHolder.itemView.isChecked = true
                else -> viewHolder.itemView.setBackgroundResource(drawableRes)
            }
            else -> when {
                viewHolder.itemView is CardView -> viewHolder.itemView
                    .setCardBackgroundColor(
                        viewHolder.itemView.context.getColorFromAttr(R.attr.cardColor)
                    )
                viewHolder.itemView is CheckBox -> viewHolder.itemView.isChecked = false
                else -> viewHolder.itemView.setBackgroundResource(0)
            }
        }
    }
}