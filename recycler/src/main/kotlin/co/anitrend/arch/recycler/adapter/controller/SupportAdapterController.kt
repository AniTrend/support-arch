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

package co.anitrend.arch.recycler.adapter.controller

import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter
import co.anitrend.arch.recycler.adapter.controller.contract.AdapterController
import co.anitrend.arch.recycler.adapter.controller.contract.StateListener
import co.anitrend.arch.recycler.shared.adapter.SupportLoadStateAdapter
import co.anitrend.arch.recycler.state.LoadStateManager
import timber.log.Timber
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Adapter controller for handling complex adapter logic
 */
class SupportAdapterController<T>(
    private val adapter: ISupportAdapter<T>,
) : AdapterController() {

    override val loadStateListeners: MutableList<StateListener> = CopyOnWriteArrayList()

    override val loadStateManager: LoadStateManager = object : LoadStateManager() {
        override fun onStateChanged(state: LoadState) {
            loadStateListeners.forEach {
                it(state.position, state)
            }
        }
    }

    /**
     * Create a [ConcatAdapter] with the provided [SupportLoadStateAdapter]s displaying the
     * [LoadState.Position.TOP] [LoadState] as a list item at the end of the presented list.
     *
     * @see SupportLoadStateAdapter
     * @see withLoadStateHeaderAndFooter
     * @see withLoadStateFooter
     */
    override fun withLoadStateHeader(header: SupportLoadStateAdapter): ConcatAdapter {
        addLoadStateListener { loadPosition, loadState ->
            Timber.d(
                "load state header updating -> position: $loadPosition | state: $loadState",
            )
            if (loadPosition != LoadState.Position.BOTTOM) {
                header.loadState = loadState
            }
        }
        adapter as RecyclerView.Adapter<*>
        return ConcatAdapter(header, adapter)
    }

    /**
     * Create a [ConcatAdapter] with the provided [SupportLoadStateAdapter]s displaying the
     * [LoadState.Position.BOTTOM] [LoadState] as a list item at the start of the presented list.
     *
     * @see SupportLoadStateAdapter
     * @see withLoadStateHeaderAndFooter
     * @see withLoadStateHeader
     */
    override fun withLoadStateFooter(footer: SupportLoadStateAdapter): ConcatAdapter {
        addLoadStateListener { loadPosition, loadState ->
            Timber.d(
                "load state footer updating -> position: $loadPosition | state: $loadState",
            )
            if (loadPosition != LoadState.Position.TOP) {
                footer.loadState = loadState
            }
        }
        adapter as RecyclerView.Adapter<*>
        return ConcatAdapter(adapter, footer)
    }

    /**
     * Create a [ConcatAdapter] with the provided [SupportLoadStateAdapter]s displaying the
     * [LoadState.Position.TOP] and [LoadState.Position.BOTTOM] [LoadState]s
     * as list items at the start and end respectively.
     *
     * @see SupportLoadStateAdapter
     * @see withLoadStateHeader
     * @see withLoadStateFooter
     */
    override fun withLoadStateHeaderAndFooter(
        header: SupportLoadStateAdapter,
        footer: SupportLoadStateAdapter,
    ): ConcatAdapter {
        addLoadStateListener { loadPosition, loadState ->
            Timber.d(
                "load state combined updating -> position: $loadPosition | state: $loadState",
            )
            when (loadPosition) {
                LoadState.Position.BOTTOM -> {
                    header.loadState = LoadState.Idle()
                    footer.loadState = loadState
                }
                else -> {
                    footer.loadState = LoadState.Idle()
                    header.loadState = loadState
                }
            }
        }
        adapter as RecyclerView.Adapter<*>
        return ConcatAdapter(header, adapter, footer)
    }
}
