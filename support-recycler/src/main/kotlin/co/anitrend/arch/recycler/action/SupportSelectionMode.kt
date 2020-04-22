package co.anitrend.arch.recycler.action

import android.view.View
import co.anitrend.arch.recycler.action.contract.ISupportSelectionListener
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.action.decorator.ISelectionDecorator
import co.anitrend.arch.recycler.model.RecyclerItem
import co.anitrend.arch.recycler.model.contract.IRecyclerItem

/**
 * A helper class for triggering action mode for
 * [co.anitrend.arch.recycler.holder.SupportViewHolder]
 *
 * @since v1.3.0
 */
class SupportSelectionMode<ID>(
    private val selectionListener: ISupportSelectionListener?
) : ISupportSelectionMode<ID> {
    /**
     * Clears all selected items in the current context and alternative stop the current action mode
     */
    override fun clearSelection() {
        TODO("Not yet implemented")
    }

    /**
     * Selects all defined items and reflects changes on the action mode
     *
     * @param selection items which can be selected
     */
    override fun selectAllItems(selection: List<ID>) {
        TODO("Not yet implemented")
    }

    /**
     * Defines whether or not this current object can be consumed as a primary click action,
     * or if in action mode should be selected or deselected.
     *
     * @param id item that has been clicked
     * @param view the view holder parent of the clicked item
     *
     * @return true if not currently in action mode otherwise false
     */
    override fun isSelectionClickable(
        view: View,
        decorator: ISelectionDecorator?,
        id: ID
    ): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Defines whether or not this current object can be consumed as a primary long click,
     * or if in action mode should start the action mode and also select or deselect the item.
     *
     * @param id item that has been clicked
     * @param view the view holder parent of the clicked item
     *
     * @return true if in action mode to inform long click listener that the
     * we have consumed the event, otherwise false
     */
    override fun isLongSelectionClickable(
        view: View,
        decorator: ISelectionDecorator?,
        id: ID
    ): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Checks if item exists in the current selection
     */
    override fun containsItem(id: ID): Boolean {
        TODO("Not yet implemented")
    }

}