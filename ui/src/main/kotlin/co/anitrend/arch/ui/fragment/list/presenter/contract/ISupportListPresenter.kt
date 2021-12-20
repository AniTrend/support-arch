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

package co.anitrend.arch.ui.fragment.list.presenter.contract

import android.view.View
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.extension.lifecycle.SupportLifecycle
import co.anitrend.arch.ui.fragment.list.contract.ISupportFragmentList
import co.anitrend.arch.ui.view.widget.contract.ISupportStateLayout

/**
 * List fragment presenter contract
 */
interface ISupportListPresenter<T> : SupportLifecycle {

    /**
     * Mirrors controller lifecycle state for creating view
     *
     * @param fragmentList The current controller attached
     * @param view Created view or null
     */
    fun onCreateView(fragmentList: ISupportFragmentList<T>, view: View?)

    /**
     * Responds to load state changes when refreshing
     *
     * @param loadState Load state or null
     */
    fun onRefreshObserverChanged(loadState: LoadState?)

    /**
     * Informs the underlying [ISupportStateLayout] of changes to the [LoadState]
     *
     * @param fragmentList Current list controller attached
     * @param loadState New state from the application
     */
    fun onNetworkObserverChanged(fragmentList: ISupportFragmentList<T>, loadState: LoadState)

    /**
     * Checks and resets swipe refresh layout and snack bar states
     */
    fun resetWidgetStates()
}
