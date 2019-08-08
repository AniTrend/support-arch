package io.wax911.support.ui.action.event

import android.view.ActionMode
import android.view.ActionMode.Callback

/**
 * Created by max on 2017/07/17.
 * Action mode trigger supportActionMode
 */
/**
 * Contract for action mode trigger
 *
 * @since 0.9.X
 */
interface ActionModeListener : Callback {

    /**
     * Called when an item is selected or deselected.
     *
     * @param mode The current ActionMode being used
     */
    fun onSelectionChanged(mode: ActionMode?, count: Int)
}
