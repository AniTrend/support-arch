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

package co.anitrend.arch.ui.fragment.list.contract

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.recycler.SupportRecyclerView
import co.anitrend.arch.recycler.adapter.SupportAdapter
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig

/**
 * Fragment list contract
 *
 * @since v0.9.X
 */
interface ISupportFragmentList<T> : SwipeRefreshLayout.OnRefreshListener {

    val onRefreshObserver: Observer<LoadState>
    val onNetworkObserver: Observer<LoadState>

    /**
     * Adapter that should be used for the recycler view, by default [StateRestorationPolicy]
     * is set to [StateRestorationPolicy.PREVENT_WHEN_EMPTY]
     */
    val supportViewAdapter: SupportAdapter<T>

    /**
     * State configuration for any underlying state representing widgets
     */
    val stateConfig: StateLayoutConfig

    /**
     * Provides a layout manager that should be used by [setRecyclerLayoutManager]
     */
    fun provideLayoutManager(): RecyclerView.LayoutManager

    /**
     * Sets a layout manager to the recycler view
     *
     * @see provideLayoutManager
     */
    fun setRecyclerLayoutManager(recyclerView: SupportRecyclerView)

    /**
     * Sets the adapter for the recycler view
     */
    fun setRecyclerAdapter(recyclerView: SupportRecyclerView)

    /**
     * Informs the underlying [SupportStateLayout] of changes to the [NetworkState]
     *
     * @param loadState New state from the application
     */
    fun changeLayoutState(loadState: LoadState)
}
