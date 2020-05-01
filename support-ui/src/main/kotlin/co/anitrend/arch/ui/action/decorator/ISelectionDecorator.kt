package co.anitrend.arch.ui.action.decorator

import android.view.View
import android.widget.CheckBox
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import co.anitrend.arch.theme.R
import co.anitrend.arch.extension.getColorFromAttr
import co.anitrend.arch.extension.getCompatColor
import co.anitrend.arch.ui.recycler.holder.SupportViewHolder

/**
 * A helper interface for applying decorations on sections for [co.anitrend.arch.ui.action.SupportActionMode]
 *
 * @since v0.9.X
 */
interface ISelectionDecorator {

    /**
     * Applies view holder which can be used to check what the [SupportViewHolder.itemView] type is
     * and apply decorations on the selected or deselected item.
     *
     * This interface has a default implementation which can be overridden
     *
     * @param view the current view holder class which has been selected or deselected
     * @param isSelected state of hte current item
     * @param drawableRes drawable overlay
     */
    fun setBackgroundColor(
        view: View,
        isSelected: Boolean,
        @DrawableRes drawableRes: Int = R.drawable.selection_frame
    ) = when {
        isSelected -> when (view) {
            is CardView -> view.setCardBackgroundColor(
                view.context.getCompatColor(R.color.colorPrimaryDark)
            )
            is CheckBox -> view.isChecked = true
            else -> view.setBackgroundResource(drawableRes)
        }
        else -> when (view) {
            is CardView -> view.setCardBackgroundColor(
                view.context.getColorFromAttr(R.attr.cardColor)
            )
            is CheckBox -> view.isChecked = false
            else -> view.setBackgroundResource(0)
        }
    }
}