package co.anitrend.arch.ui.action.contract

import co.anitrend.arch.ui.action.decorator.SelectionDecorator
import co.anitrend.arch.ui.recycler.adapter.SupportViewAdapter
import co.anitrend.arch.ui.recycler.holder.SupportViewHolder

/**
 * Contract for action mode implementation
 *
 * @since 0.9.X
 */
interface ISupportActionMode<T> {

    /**
     * Clears all selected items in the current context and alternative stop the current action mode
     */
    fun clearSelection()

    /**
     * Selects all defined items and reflects changes on the action mode
     *
     * @param selection items which can be selected
     */
    fun selectAllItems(selection: List<T>)

    /**
     * Defines whether or not this current object can be consumed as a primary click action,
     * or if in action mode should be selected or deselected.
     *
     * @param objectItem recycler view item that has been clicked
     * @param viewHolder the view holder parent of the clicked item
     *
     * @return true if not currently in action mode otherwise false
     *
     *  @see [SupportViewHolder.isClickable]
     */
    fun isSelectionClickable(viewHolder: SupportViewHolder<T>, objectItem: T?): Boolean

    /**
     * Defines whether or not this current object can be consumed as a primary long click,
     * or if in action mode should start the action mode and also select or deselect the item.
     *
     * @param objectItem recycler view item that has been clicked
     * @param viewHolder the view holder parent of the clicked item
     *
     * @return true if in action mode to inform long click listener that the
     * we have consumed the event, otherwise false
     *
     * @see [SupportViewHolder.isLongClickable]
     */
    fun isLongSelectionClickable(viewHolder: SupportViewHolder<T>, objectItem: T?): Boolean

    /**
     * All selected items or an empty list if there are none. This list only holds data
     * while the action mode has not be stopped or destroyed, after it will be cleared
     *
     * @return all the selected items
     */
    fun getAllSelectedItems(): List<T>

    /**
     * @return selection mode decorator, if none was assigned the default implementation is used
     *
     * @see [SelectionDecorator.setBackgroundColor]
     */
    fun getSelectionDecorator(): SelectionDecorator<T>

    /**
     * Allows your to see your own implementation of how selected or unselected items
     * should be decorated as they are selected and un-selected
     *
     * @param selectionDecorator implementation of a custom decorator
     */
    fun setSelectionDecorator(selectionDecorator: SelectionDecorator<T>)

    /**
     * Sets current view adapter that should be used for various events, triggers, e.t.c
     *
     * @param supportViewAdapter current recycler view adapter
     */
    fun setRecyclerViewAdapter(supportViewAdapter: SupportViewAdapter<*>)
}