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

package co.anitrend.arch.recycler.adapter.controller.contract

import androidx.recyclerview.widget.ConcatAdapter
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.recycler.shared.adapter.SupportLoadStateAdapter
import co.anitrend.arch.recycler.state.LoadStateManager

typealias StateListener = (LoadState.Position, LoadState) -> Unit

abstract class AdapterController {
    protected abstract val loadStateListeners: MutableList<StateListener>

    protected abstract val loadStateManager: LoadStateManager

    /**
     * Add a [LoadState] listener to observe the loading state of the current [List].
     *
     * As new Lists are submitted and displayed, the listener will be notified to reflect
     * current [LoadState.Loading.Position.TOP] and [LoadState.Loading.Position.BOTTOM].
     *
     * @param listener [LoadState] listener to receive updates.
     *
     * @see removeLoadStateListener
     */
    protected fun addLoadStateListener(listener: (LoadState.Position, LoadState) -> Unit) {
        loadStateListeners.removeAll { it == listener }
        loadStateManager.dispatchCurrentLoadState(listener)
        loadStateListeners.add(listener)
    }

    /**
     * Dispatches state changes to a state manager
     */
    fun onLoadStateChanged(loadState: LoadState) = loadStateManager.setState(loadState)

    /**
     * Create a [ConcatAdapter] with the provided [SupportLoadStateAdapter]s displaying the
     * [LoadState.Loading.Position.TOP] [LoadState] as a list item at the end of the presented list.
     *
     * @see SupportLoadStateAdapter
     * @see withLoadStateHeaderAndFooter
     * @see withLoadStateFooter
     */
    abstract fun withLoadStateHeader(header: SupportLoadStateAdapter): ConcatAdapter

    /**
     * Create a [ConcatAdapter] with the provided [SupportLoadStateAdapter]s displaying the
     * [LoadState.Loading.Position.BOTTOM] [LoadState] as a list item at the start of the presented list.
     *
     * @see SupportLoadStateAdapter
     * @see withLoadStateHeaderAndFooter
     * @see withLoadStateHeader
     */
    abstract fun withLoadStateFooter(footer: SupportLoadStateAdapter): ConcatAdapter

    /**
     * Create a [ConcatAdapter] with the provided [SupportLoadStateAdapter]s displaying the
     * [LoadState.Loading.Position.TOP] and [LoadState.Loading.Position.BOTTOM] [LoadState]s
     * as list items at the start and end respectively.
     *
     * @see SupportLoadStateAdapter
     * @see withLoadStateHeader
     * @see withLoadStateFooter
     */
    abstract fun withLoadStateHeaderAndFooter(
        header: SupportLoadStateAdapter,
        footer: SupportLoadStateAdapter,
    ): ConcatAdapter

    fun onPause() {
        // loadStateListeners.clear()
    }
}
