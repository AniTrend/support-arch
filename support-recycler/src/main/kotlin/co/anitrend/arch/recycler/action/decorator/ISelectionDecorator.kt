/**
 * Copyright 2021 AniTrend
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.anitrend.arch.recycler.action.decorator

import android.view.View
import android.widget.CheckBox
import androidx.annotation.DrawableRes
import co.anitrend.arch.recycler.R

/**
 * A helper interface for applying decorations on sections for
 * [co.anitrend.arch.recycler.action.contract.ISupportSelectionMode].
 *
 * This interface has a default implementation which can be overridden
 *
 * @since v1.3.0
 */
interface ISelectionDecorator {

    /**
     * Applies decorations on the selected or deselected item.
     *
     * @param view the current view holder class which has been selected or deselected
     * @param isSelected state of hte current item
     * @param drawableRes drawable overlay
     */
    fun decorateUsing(
        view: View,
        isSelected: Boolean,
        @DrawableRes drawableRes: Int = R.drawable.selection_frame
    ) = when (isSelected) {
        true -> when (view) {
            is CheckBox -> view.isChecked = true
            else -> view.setBackgroundResource(drawableRes)
        }
        else -> when (view) {
            is CheckBox -> view.isChecked = false
            else -> view.setBackgroundResource(0)
        }
    }
}
