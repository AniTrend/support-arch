package co.anitrend.arch.recycler.model.contract

import co.anitrend.arch.recycler.action.decorator.ISelectionDecorator

/**
 * Contract for recycler item
 *
 * @property id id of the item view
 * @property layout layout to inflate
 * @property isClickable whether or not the element supports top level click actions
 * @property isLongClickable whether or not the element supports top level long click actions
 * @property supportsActionMode whether or not the element can trigger action mode
 * @property decorator provides styling information
 *
 * @since v1.3.0
 */
interface IRecyclerItem {
    val id: Long

    val layout: Int

    val isClickable: Boolean
    val isLongClickable: Boolean
    val supportsActionMode: Boolean

    val decorator: ISelectionDecorator
}