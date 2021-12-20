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

package co.anitrend.arch.domain.state

/**
 * Contract for user interface state which UI components can use to interact with state
 *
 * @property loadState Network request status to show to the user
 * @property refreshState Refresh status to show to the user. Separate from [loadState],
 * this value is importantly only when refresh is requested
 * @property refresh Refreshes & invalidates underlying data source fetches it from scratch.
 * @property retry Retries any failed requests.
 */
abstract class UiState<out T> {
    abstract val loadState: T
    abstract val refreshState: T
    abstract val refresh: suspend () -> Unit
    abstract val retry: suspend () -> Unit
}
