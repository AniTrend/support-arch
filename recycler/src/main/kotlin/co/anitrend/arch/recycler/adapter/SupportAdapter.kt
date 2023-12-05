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

package co.anitrend.arch.recycler.adapter

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ConcatAdapter
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter
import co.anitrend.arch.recycler.adapter.controller.contract.AdapterController
import co.anitrend.arch.recycler.shared.adapter.SupportLoadStateAdapter

/**
 * Additional contract for common adapter behaviour
 */
interface SupportAdapter<T> : ISupportAdapter<T> {
    /**
     * Internal use only indicator for checking against the use of a
     * concat adapter for headers and footers, which is in turn used
     * to figure out how to get the view holder id
     *
     * @see withLoadStateHeader
     * @see withLoadStateFooter
     * @see withLoadStateHeaderAndFooter
     */
    var isUsingConcatAdapter: Boolean

    /**
     * Special dispatcher controller for custom logic handling
     */
    val controller: AdapterController

    /**
     * Sets loading state
     */
    fun setLoadState(state: LoadState) {
        controller.onLoadStateChanged(state)
    }

    /**
     * Create a [ConcatAdapter] with the provided [SupportLoadStateAdapter]s
     *
     * @see AdapterController.withLoadStateHeader
     */
    fun withLoadStateHeader(header: SupportLoadStateAdapter): ConcatAdapter {
        isUsingConcatAdapter = true
        return controller.withLoadStateHeader(header)
    }

    /**
     * Create a [ConcatAdapter] with the provided [SupportLoadStateAdapter]s
     *
     * @see AdapterController.withLoadStateFooter
     */
    fun withLoadStateFooter(footer: SupportLoadStateAdapter): ConcatAdapter {
        isUsingConcatAdapter = true
        return controller.withLoadStateFooter(footer)
    }

    /**
     * Create a [ConcatAdapter] with the provided [SupportLoadStateAdapter]s
     *
     * @see AdapterController.withLoadStateHeaderAndFooter
     */
    fun withLoadStateHeaderAndFooter(
        header: SupportLoadStateAdapter,
        footer: SupportLoadStateAdapter,
    ): ConcatAdapter {
        isUsingConcatAdapter = true
        return controller.withLoadStateHeaderAndFooter(header, footer)
    }

    /**
     * Notifies that `ON_PAUSE` event occurred.
     *
     * This method will be called before the [LifecycleOwner]'s `onPause` method
     * is called.
     *
     * @param owner the component, whose state was changed
     */
    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        controller.onPause()
    }
}
