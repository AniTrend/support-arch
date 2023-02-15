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

package co.anitrend.arch.recycler.common

import android.view.View
import co.anitrend.arch.domain.entities.LoadState

/**
 * Clickable item base class
 */
sealed class ClickableItem {

    open val clickType: ClickType = ClickType.SHORT

    object None : ClickableItem()

    /**
     * A data clickable item with a click type of [ClickType]
     */
    data class Data<T>(
        val data: T,
        val view: View,
        override val clickType: ClickType = ClickType.SHORT,
    ) : ClickableItem()

    /**
     * A default clickable item with a click type of [ClickType]
     */
    data class Default(
        val view: View,
        override val clickType: ClickType = ClickType.SHORT,
    ) : ClickableItem()

    /**
     * Clickable item for footer views
     */
    data class State(
        val state: LoadState,
        val view: View,
    ) : ClickableItem()

    enum class ClickType {
        SHORT, LONG
    }
}
