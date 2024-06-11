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

package co.anitrend.arch.core.model

import androidx.lifecycle.LiveData
import co.anitrend.arch.domain.entities.LoadState

/**
 * A helper contract for view model items
 *
 * @property model observable model from a use case output
 * @property loadState observable network state from underlying sources
 *
 * @param R type of your [model]
 *
 * @since v1.3.0
 */
interface ISupportViewModelState<R> {
    val model: LiveData<R>
    val loadState: LiveData<LoadState>

    /**
     * Triggers use case to perform a retry operation
     */
    suspend fun retry()

    /**
     * Triggers use case to perform refresh operation
     */
    suspend fun refresh()
}
