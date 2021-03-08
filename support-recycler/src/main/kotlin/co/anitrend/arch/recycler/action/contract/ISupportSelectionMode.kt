package co.anitrend.arch.recycler.action.contract

import android.view.ActionMode
import android.view.View
import co.anitrend.arch.recycler.action.decorator.ISelectionDecorator

/**
 * Contract for action mode implementation
 *
 * @since v1.3.0
 */
interface ISupportSelectionMode<ID> {

    /**
     * Clears all selected items in the current context and
     * alternative stops the current action mode [mode]
     *
     * @param mode action mode currently in use
     */
    fun clearSelection(mode: ActionMode?)


    /**
     * Selects all defined items and reflects changes on the action mode
     *
     * @param selection items which can be selected
     */
    fun selectAllItems(selection: List<ID>)

    /**
     * @return list of selected items
     */
    fun selectedItems(): List<ID>

    /**
     * Defines whether or not this current object can be consumed as a primary click action,
     * or if in action mode should be selected or deselected.
     *
     * @param id item that has been clicked
     * @param decorator selection or deselection decorator
     * @param view the view holder parent of the clicked item
     *
     * @return true if not currently in action mode otherwise false
     */
    fun isSelectionClickable(view: View, decorator: ISelectionDecorator, id: ID): Boolean

    /**
     * Defines whether or not this current object can be consumed as a primary long click,
     * or if in action mode should start the action mode and also select or deselect the item.
     *
     * @param id item that has been clicked
     * @param decorator selection or deselection decorator
     * @param view the view holder parent of the clicked item
     *
     * @return true if in action mode to inform long click listener that the
     * we have consumed the event, otherwise false
     */
    fun isLongSelectionClickable(view: View, decorator: ISelectionDecorator, id: ID): Boolean

    /**
     * Checks if item exists in the current selection
     */
    fun containsItem(id: ID): Boolean
}