package co.anitrend.arch.ui.action

import android.view.ActionMode
import android.view.View
import co.anitrend.arch.core.presenter.contract.ISupportPresenter
import co.anitrend.arch.ui.action.contract.ISupportActionMode
import co.anitrend.arch.ui.action.decorator.ISelectionDecorator
import co.anitrend.arch.ui.action.event.ActionModeListener
import co.anitrend.arch.ui.recycler.adapter.contract.ISupportViewAdapter
import co.anitrend.arch.ui.recycler.holder.SupportViewHolder
import java.util.*


/**
 * A helper class for triggering action mode from [SupportViewHolder]
 *
 * @since v0.9.X
 */
@Deprecated("May be removed in 1.3.0-stable when support-recycler module reaches stable")
class SupportActionMode<T>(
    private val actionModeListener: ActionModeListener?,
    private val presenter: ISupportPresenter?
): ISupportActionMode<T> {

    private var actionMode: ActionMode? = null

    private var supportViewAdapter: ISupportViewAdapter<*>? = null

    private val selectedItems: MutableList<T> = ArrayList()

    private fun startActionMode(view: View) {
        if (selectedItems.isEmpty())
            actionMode = view.startActionMode(actionModeListener)
    }

    private fun selectItem(view: View, supportDecorator: ISelectionDecorator?, objectItem: T?) {
        if (objectItem != null) {
            startActionMode(view)
            selectedItems.add(objectItem)
            supportDecorator?.setBackgroundColor(view, true)
            actionModeListener?.onSelectionChanged(actionMode, selectedItems.size)
        }
    }

    private fun deselectItem(view: View, supportDecorator: ISelectionDecorator?, objectItem: T?) {
        if (objectItem != null) {
            selectedItems.remove(objectItem)
            supportDecorator?.setBackgroundColor(view, false)
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
        supportViewAdapter?.updateSelection()
    }

    /**
     * Selects all defined items and reflects changes on the action mode
     *
     * @param selection items which can be selected
     */
    override fun selectAllItems(selection: List<T>) {
        selectedItems.clear()
        selectedItems.addAll(selection)
        supportViewAdapter?.updateSelection()
        actionModeListener?.onSelectionChanged(actionMode, selectedItems.size)
    }

    /**
     * Defines whether or not this current object can be consumed as a primary click action,
     * or if in action mode should be selected or deselected.
     *
     * @param objectItem recycler view item that has been clicked
     * @param view the view holder parent of the clicked item
     *
     * @return true if not currently in action mode otherwise false
     *
     *  @see [SupportViewHolder.isClickable]
     */
    override fun isSelectionClickable(
        view: View,
        decorator: ISelectionDecorator?,
        objectItem: T?
    ) = when {
        presenter?.isActionModeEnabled != true || selectedItems.isEmpty() -> true
        else -> when (selectedItems.contains(objectItem)) {
            true -> { deselectItem(view, decorator, objectItem); false }
            else -> { selectItem(view, decorator, objectItem); false }
        }
    }

    /**
     * Defines whether or not this current object can be consumed as a primary long click,
     * or if in action mode should start the action mode and also select or deselect the item.
     *
     * @param objectItem recycler view item that has been clicked
     * @param view the view holder parent of the clicked item
     *
     * @return true if in action mode to inform long click listener that the
     * we have consumed the event, otherwise false
     *
     * @see [SupportViewHolder.isLongClickable]
     */
    override fun isLongSelectionClickable(
        view: View,
        decorator: ISelectionDecorator?,
        objectItem: T?
    ) = when {
        presenter?.isActionModeEnabled != true -> false
        else ->  when (selectedItems.contains(objectItem)) {
            true -> { deselectItem(view, decorator, objectItem); true }
            else -> { selectItem(view, decorator, objectItem); true }
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
     * Checks if item exists in the current selection
     */
    override fun containsItem(item: T?): Boolean {
        return if (item == null && selectedItems.isNotEmpty())
            false
        else selectedItems.contains(item)
    }

    /**
     * Sets current view adapter that should be used for various events, triggers, e.t.c
     *
     * @param supportViewAdapter current recycler view adapter
     */
    override fun setRecyclerViewAdapter(supportViewAdapter: ISupportViewAdapter<*>) {
        this.supportViewAdapter = supportViewAdapter
    }
}