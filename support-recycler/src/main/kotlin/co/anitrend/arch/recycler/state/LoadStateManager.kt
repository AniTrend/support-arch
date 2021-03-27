package co.anitrend.arch.recycler.state

import co.anitrend.arch.domain.entities.LoadState

/**
 * Copied from androidx.paging.PagedList.LoadStateManager
 *
 * ```
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ```
 */
abstract class LoadStateManager {
    var startState: LoadState = LoadState.Idle()
    var endState: LoadState = LoadState.Idle()

    /** Updates load state */
    fun setState(state: LoadState) {
        when (state.position) {
            LoadState.Position.TOP -> {
                if (startState == state) return
                    startState = state
            }
            LoadState.Position.BOTTOM -> {
                if (endState == state) return
                    endState = state
            }
            else -> { }
        }
        onStateChanged(state)
    }

    /**
     * Action handler for state changes
     *
     * @param state New state
     */
    abstract fun onStateChanged(state: LoadState)

    /**
     * Invokes [callback] with the appropriate states
     */
    fun dispatchCurrentLoadState(callback: (LoadState.Position, LoadState) -> Unit) {
        callback(LoadState.Position.TOP, startState)
        callback(LoadState.Position.BOTTOM, endState)
    }
}