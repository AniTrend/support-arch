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

package co.anitrend.arch.ui.fragment.list.presenter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.extension.lifecycle.SupportLifecycle
import co.anitrend.arch.recycler.SupportRecyclerView
import co.anitrend.arch.recycler.extensions.isEmpty
import co.anitrend.arch.ui.extension.configureWidgetBehaviorWith
import co.anitrend.arch.ui.extension.onResponseResetStates
import co.anitrend.arch.ui.fragment.list.contract.ISupportFragmentList
import co.anitrend.arch.ui.fragment.list.presenter.contract.ISupportListPresenter
import co.anitrend.arch.ui.view.widget.contract.ISupportStateLayout

/**
 * Presenter for list fragments
 */
abstract class SupportListPresenter<T> : ISupportListPresenter<T> {

    abstract val recyclerView: RecyclerView
    abstract val stateLayout: ISupportStateLayout
    abstract val swipeRefreshLayout: SwipeRefreshLayout?

    override fun onCreateView(fragmentList: ISupportFragmentList<T>, view: View?) {
        stateLayout.stateConfigFlow.value = fragmentList.stateConfig

        swipeRefreshLayout?.apply {
            configureWidgetBehaviorWith()
            setOnRefreshListener(fragmentList)
        }

        with(recyclerView) {
            setHasFixedSize(true)
            if (this is SupportRecyclerView) {
                fragmentList.setRecyclerAdapter(this)
                fragmentList.setRecyclerLayoutManager(this)
            }
            isNestedScrollingEnabled = true
        }
    }

    override fun onRefreshObserverChanged(loadState: LoadState?) {
        swipeRefreshLayout?.isRefreshing = loadState is LoadState.Loading
    }

    /**
     * Informs the underlying [ISupportStateLayout] of changes to the [LoadState]
     *
     * @param loadState New state from the application
     */
    override fun onNetworkObserverChanged(
        fragmentList: ISupportFragmentList<T>,
        loadState: LoadState
    ) {
        if (
            !fragmentList.supportViewAdapter.isEmpty() ||
            loadState.position != LoadState.Position.UNDEFINED
        ) {
            stateLayout.loadStateFlow.value = LoadState.Idle()
            fragmentList.supportViewAdapter.setLoadState(loadState)
        } else {
            stateLayout.loadStateFlow.value = loadState
        }
        resetWidgetStates()
    }

    /**
     * Checks and resets swipe refresh layout and snack bar states
     */
    override fun resetWidgetStates() {
        swipeRefreshLayout?.onResponseResetStates()
    }

    /**
     * Triggered when the lifecycleOwner reaches it's onStop state
     *
     * @see [androidx.lifecycle.LifecycleOwner]
     */
    override fun onStop() {
        super.onStop()
        swipeRefreshLayout?.setOnRefreshListener(null)
    }

    /**
     * Triggered when the lifecycleOwner reaches it's onDestroy state
     *
     * @see [androidx.lifecycle.LifecycleOwner]
     */
    override fun onDestroy() {
        super.onDestroy()
        if (recyclerView is SupportLifecycle)
            (recyclerView as SupportLifecycle).onDestroy()
    }
}
