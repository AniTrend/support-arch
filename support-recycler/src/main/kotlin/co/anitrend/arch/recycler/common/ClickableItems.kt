package co.anitrend.arch.recycler.common

import android.view.View
import co.anitrend.arch.domain.entities.NetworkState

/**
 * Clickable item base class
 */
sealed class ClickableItem(
    open val clickType: ClickType = ClickType.SHORT
)

/**
 * Clickable item for footer views
 */
class StateClickableItem(
    val state: NetworkState?,
    val view: View
) : ClickableItem()

/**
 * A default clickable item with a click type of [ClickType]
 */
class DefaultClickableItem<T>(
    val data: T?,
    val view: View,
    override val clickType: ClickType = ClickType.SHORT
) : ClickableItem()

enum class ClickType {
    SHORT, LONG
}