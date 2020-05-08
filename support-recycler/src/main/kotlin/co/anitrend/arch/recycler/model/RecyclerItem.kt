package co.anitrend.arch.recycler.model

import co.anitrend.arch.recycler.action.decorator.ISelectionDecorator
import co.anitrend.arch.recycler.model.contract.IRecyclerItem

/**
 * A wrapper for items that can be displayed inside the recycler view
 *
 * @since v1.3.0
 *
 * @see IRecyclerItem
 */
abstract class RecyclerItem(
    override val id: Long
) : IRecyclerItem {

    override val supportsSelectionMode: Boolean = false

    /**
     * Decorator that can be used to style this item when it is selected or unselected
     */
    override val decorator =
        object : ISelectionDecorator { }
}