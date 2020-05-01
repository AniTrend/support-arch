package co.anitrend.arch.recycler.action.decorator

import android.view.View
import android.widget.CheckBox
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import co.anitrend.arch.extension.getColorFromAttr
import co.anitrend.arch.extension.getCompatColor
import co.anitrend.arch.recycler.R

/**
 * A helper interface for applying decorations on sections for
 * [co.anitrend.arch.recycler.action.SupportSelectionMode].
 *
 * This interface has a default implementation which can be overridden
 *
 * @since v1.3.0
 */
interface ISelectionDecorator {

    /**
     * Applies decorations on the selected or deselected item.
     *
     * @param view the current view holder class which has been selected or deselected
     * @param isSelected state of hte current item
     * @param drawableRes drawable overlay
     */
    fun decorateUsing(
        view: View,
        isSelected: Boolean,
        @DrawableRes drawableRes: Int = R.drawable.selection_frame
    ) = when (isSelected) {
        true -> when (view) {
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