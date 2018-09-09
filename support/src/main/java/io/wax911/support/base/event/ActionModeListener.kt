package io.wax911.support.base.event

import android.view.ActionMode
import android.view.ActionMode.Callback

/**
 * Created by max on 2017/07/17.
 * Action mode trigger supportActionUtil
 */

interface ActionModeListener : Callback {
    fun onSelectionChanged(actionMode: ActionMode, count: Int)
}
