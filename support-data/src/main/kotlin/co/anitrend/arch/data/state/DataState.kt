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

package co.anitrend.arch.data.state

import co.anitrend.arch.data.source.contract.IDataSource
import co.anitrend.arch.data.source.core.contract.AbstractDataSource
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.domain.state.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Model that view models create for UI components to observe on
 *
 * @param model LiveData for the UI to observe
 * @param loadState Load status to show to the user
 * @param refreshState Refresh status to show to the user. Separate from [loadState],
 * this value is importantly only when refresh is requested
 * @param refresh Refreshes & invalidates underlying data source fetches it from scratch.
 * @param retry Retries any failed requests.
 */
data class DataState<T> internal constructor(
    val model: Flow<T>,
    override val loadState: Flow<LoadState>,
    override val refreshState: Flow<LoadState>,
    override val refresh: suspend () -> Unit,
    override val retry: suspend () -> Unit
) : UiState<Flow<LoadState>>() {

    companion object {

        /**
         * Helper for creating a user interface state using a data source
         *
         * @param model The requested result as an observable
         *
         * @see AbstractDataSource
         */
        infix fun <T> IDataSource.create(
            model: Flow<T>
        ): DataState<T> {
            val refreshTrigger: MutableStateFlow<LoadState> = MutableStateFlow(LoadState.Idle())

            return DataState(
                model = model,
                loadState = loadState,
                refreshState = refreshTrigger,
                refresh = {
                    refreshTrigger.value = LoadState.Loading()
                    refresh()
                    refreshTrigger.value = LoadState.Success()
                },
                retry = {
                    retryFailed()
                }
            )
        }
    }
}
