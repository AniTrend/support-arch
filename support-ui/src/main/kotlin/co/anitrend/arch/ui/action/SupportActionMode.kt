package co.anitrend.arch.ui.action

import android.view.ActionMode
import co.anitrend.arch.core.presenter.SupportPresenter
import co.anitrend.arch.ui.action.contract.ISupportActionMode
import co.anitrend.arch.ui.action.decorator.SelectionDecorator
import co.anitrend.arch.ui.action.event.ActionModeListener
import co.anitrend.arch.ui.recycler.adapter.SupportViewAdapter
import co.anitrend.arch.ui.recycler.holder.SupportViewHolder
import java.util.*


/**
 * A helper class for triggering action mode from [SupportViewHolder]
 *
 * @since 0.9.X
 */
class SupportActionMode<T>(
    private val actionModeListener: ActionModeListener?,
    private val presenter: SupportPresenter<*>?
): ISupportActionMode<T> {

    private var actionMode: ActionMode? = null

    private var supportViewAdapter: SupportViewAdapter<*>? = null

    private val selectedItems: MutableList<T> = ArrayList()

    private var selectionDecorator: SelectionDecorator<T> =
        object: SelectionDecorator<T> {

        }

    private fun startActionMode(viewHolder: SupportViewHolder<T>) {
        if (selectedItems.isEmpty())
            actionMode = viewHolder.itemView.startActionMode(actionModeListener)
    }

    private fun selectItem(viewHolder: SupportViewHolder<T>, objectItem: T?) {
        if (objectItem != null) {
            startActionMode(viewHolder)
            selectedItems.add(objectItem)
            selectionDecorator.setBackgroundColor(viewHolder, true)
            actionModeListener?.onSelectionChanged(actionMode, selectedItems.size)
        }
    }

    private fun deselectItem(viewHolder: SupportViewHolder<T>, objectItem: T?) {
        if (objectItem != null) {
            selectedItems.remove(objectItem)
            selectionDecorator.setBackgroundColor(viewHolder, false)
            when (selectedItems.isEmpty()) {
                true -> actionMode?.finish()
                false -> actionModeListener?.onSelectionChanged(actionMode, selectedItems.size)
            }
        }
    }

    /**
     * Clears all selected items in the current context and alternative stop the current action mode
     */
    override fun clearSelection() {
        actionMode?.finish()
        selectedItems.clear()
        supportViewAdapter?.notifyDataSetChanged()
    }

    /**
     * Selects all defined items and reflects changes on the action mode
     *
     * @param selection items which can be selected
     */
    override fun selectAllItems(selection: List<T>) {
        selectedItems.clear()
        selectedItems.addAll(selection)
        supportViewAdapter?.notifyDataSetChanged()
        actionModeListener?.onSelectionChanged(actionMode, selectedItems.size)
    }

    /**
     * Defines whether or not this current object can be consumed as a primary click action,
     * or if in action mode should be selected or deselected.
     *
     * @param objectItem recycler view item that has been clicked
     * @param viewHolder the view holder parent of the clicked item
     *
     * @return true if not currently in action mode otherwise false
     *
     *  @see SupportViewHolder.isClickable
     */
    override fun isSelectionClickable(viewHolder: SupportViewHolder<T>, objectItem: T?) = when {
        presenter?.isActionModeEnabled != true || selectedItems.isEmpty() -> true
        else -> when (selectedItems.contains(objectItem)) {
            true -> { deselectItem(viewHolder, objectItem); false }
            else -> { selectItem(viewHolder, objectItem); false }
        }
    }

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
     * @see SupportViewHolder.isLongClickable
     */
    override fun isLongSelectionClickable(viewHolder: SupportViewHolder<T>, objectItem: T?) = when {
        presenter?.isActionModeEnabled != true -> false
        else ->  when (selectedItems.contains(objectItem)) {
            true -> { deselectItem(viewHolder, objectItem); true }
            else -> { selectItem(viewHolder, objectItem); true }
        }
    }


    /**
     * All selected items or an empty list if there are none. This list only holds data
     * while the action mode has not be stopped or destroyed, after it will be cleared
     *
     * @return all the selected items
     */
    override fun getAllSelectedItems(): List<T> = selectedItems

    /**
     * @return selection mode decorator, if none was assigned the default implementation is used
     *
     * @see [SelectionDecorator.setBackgroundColor]
     */
    override fun getSelectionDecorator() = selectionDecorator

    /**
     * Allows your to see your own implementation of how selected or unselected items
     * should be decorated as they are selected and un-selected
     *
     * @param selectionDecorator implementation of a custom decorator
     */
    override fun setSelectionDecorator(selectionDecorator: SelectionDecorator<T>) {
        this.selectionDecorator = selectionDecorator
    }

    /**
     * Sets current view adapter that should be used for various events, triggers, e.t.c
     *
     * @param supportViewAdapter current recycler view adapter
     */
    override fun setRecyclerViewAdapter(supportViewAdapter: SupportViewAdapter<*>) {
        this.supportViewAdapter = supportViewAdapter
    }
}