package io.wax911.support.util

import android.view.ActionMode
import android.widget.CheckBox
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import io.wax911.support.R
import io.wax911.support.base.event.ActionModeListener
import io.wax911.support.custom.recycler.SupportViewHolder
import io.wax911.support.getColorFromAttr
import io.wax911.support.getCompatColor
import io.wax911.support.replaceWith
import java.util.*

/**
 * Created by max on 2017/07/17.
 * Custom action mode holder class
 */

class SupportActionUtil<T>(private val actionModeListener: ActionModeListener, private val isEnabled: Boolean) {

    private lateinit var actionMode: ActionMode

    lateinit var recyclerAdapter: RecyclerView.Adapter<*>

    val selectedItems: MutableList<T> by lazy { ArrayList<T>() }

    private fun startActionMode(viewHolder: SupportViewHolder<T>) {
        if (selectedItems.isEmpty())
            actionMode = viewHolder.itemView.startActionMode(actionModeListener)
    }

    fun clearSelection() {
        actionMode.finish()
        selectedItems.clear()
        recyclerAdapter.notifyDataSetChanged()
    }

    private fun selectItem(viewHolder: SupportViewHolder<T>, objectItem: T) {
        startActionMode(viewHolder)
        selectedItems.add(objectItem)
        setBackgroundColor(viewHolder, true)
        actionModeListener.onSelectionChanged(actionMode, selectedItems.size)
    }

    private fun deselectItem(viewHolder: SupportViewHolder<T>, objectItem: T) {
        selectedItems.remove(objectItem)
        setBackgroundColor(viewHolder, false)
        when (selectedItems.isEmpty()) {
            true -> actionMode.finish()
            false -> actionModeListener.onSelectionChanged(actionMode, selectedItems.size)
        }
    }

    fun isSelectionClickable(viewHolder: SupportViewHolder<T>, objectItem: T): Boolean = when {
        !isEnabled || selectedItems.isEmpty() -> true
        else -> when (selectedItems.contains(objectItem)) {
            true -> { deselectItem(viewHolder, objectItem); false }
            else -> { selectItem(viewHolder, objectItem); false }
        }

    }

    fun isLongSelectionClickable(viewHolder: SupportViewHolder<T>, objectItem: T): Boolean = when {
        !isEnabled -> true
        else ->  when (selectedItems.contains(objectItem)) {
            true -> { deselectItem(viewHolder, objectItem); false }
            else -> { selectItem(viewHolder, objectItem); false }
        }
    }

    fun setBackgroundColor(viewHolder: SupportViewHolder<T>, isSelected: Boolean) = when {
        isSelected -> when {
            viewHolder.itemView is CardView -> viewHolder.itemView
                    .setCardBackgroundColor(
                            viewHolder.context.getCompatColor(R.color.colorTextGrey2nd)
                    )
            viewHolder.itemView is CheckBox -> viewHolder.itemView.isChecked = true
            else -> viewHolder.itemView.setBackgroundResource(R.drawable.selection_frame)
        }
        else -> when {
            viewHolder.itemView is CardView -> viewHolder.itemView
                    .setCardBackgroundColor(
                            viewHolder.context.getColorFromAttr(R.attr.cardColor)
                    )
            viewHolder.itemView is CheckBox -> viewHolder.itemView.isChecked = false
            else -> viewHolder.itemView.setBackgroundResource(0)
        }
    }

    fun selectAllItems(selectableItems: List<T>) {
        selectedItems.replaceWith(selectableItems)
        recyclerAdapter.notifyDataSetChanged()
        actionModeListener.onSelectionChanged(actionMode, selectedItems.size)
    }
}