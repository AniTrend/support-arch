package co.anitrend.arch.recycler.common

import android.view.View
import co.anitrend.arch.domain.entities.LoadState

/**
 * Clickable item base class
 */
sealed class ClickableItem {

    open val clickType: ClickType = ClickType.SHORT

    object None : ClickableItem()

    /**
     * A data clickable item with a click type of [ClickType]
     */
    data class Data<T>(
        val data: T,
        val view: View,
        override val clickType: ClickType = ClickType.SHORT
    ) : ClickableItem()

    /**
     * A default clickable item with a click type of [ClickType]
     */
    data class Default(
        val view: View,
        override val clickType: ClickType = ClickType.SHORT
    ) : ClickableItem()

    /**
     * Clickable item for footer views
     */
    data class State(
        val state: LoadState,
        val view: View
    ) : ClickableItem()

    enum class ClickType {
        SHORT, LONG
    }
}