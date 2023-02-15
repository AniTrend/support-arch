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

package co.anitrend.arch.ui.view.widget.contract

import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Contract for state layout
 */
interface ISupportStateLayout : CustomView {

    val isLoading: Boolean

    val isError: Boolean

    val isContent: Boolean

    /**
     * Observable for publishing states to this widget
     */
    val loadStateFlow: MutableStateFlow<LoadState>

    /**
     * Configuration for that should be used by the different view states
     */
    val stateConfigFlow: MutableStateFlow<StateLayoutConfig?>

    /**
     * Observable for click interactions, which returns the current network state
     */
    val interactionFlow: Flow<ClickableItem>

    companion object {
        /** First view inflated index which is loading view */
        const val LOADING_VIEW = 0

        /** Second view inflated in this case the error view */
        const val ERROR_VIEW = 1

        /** Third inflated view should be the current view wrapped by this layout */
        const val CONTENT_VIEW = 2
    }
}
