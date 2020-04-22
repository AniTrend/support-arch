package co.anitrend.arch.recycler.action.contract

import android.view.ActionMode
import android.view.ActionMode.Callback

/**
 * Contract for action mode when it is active
 *
 * @since v1.3.0
 */
interface ISupportSelectionListener : Callback  {

    /**
     * Called when an item is selected or deselected.
     *
     * @param mode The current ActionMode being used
     */
    fun onSelectionChanged(mode: ActionMode?, count: Int)
}