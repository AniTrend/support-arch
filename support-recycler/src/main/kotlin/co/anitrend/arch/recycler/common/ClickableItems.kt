package co.anitrend.arch.recycler.common

import android.view.View
import co.anitrend.arch.recycler.common.DefaultClickableItem.ClickType

/**
 * Clickable item base class
 */
sealed class ClickableItem(
    val view: View
)

/**
 * Clickable item for footer views
 */
class FooterClickableItem(
    view: View
) : ClickableItem(view)

/**
 * A default clickable item with a click type of [ClickType]
 */
class DefaultClickableItem<T>(
    val clickType: ClickType = ClickType.SHORT,
    val data: T?,
    view: View
) : ClickableItem(view) {
    enum class ClickType {
        SHORT, LONG
    }
}