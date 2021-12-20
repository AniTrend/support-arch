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

package co.anitrend.arch.data.source.contract

import co.anitrend.arch.data.request.AbstractRequestHelper
import co.anitrend.arch.domain.entities.LoadState
import kotlinx.coroutines.flow.Flow

interface IDataSource {

    /**
     * Request helper that controls the flow of requests to the implementing data source to avoid
     * multiple requests of the same type before others are completed for this instance
     *
     * @see AbstractRequestHelper
     */
    val requestHelper: AbstractRequestHelper

    /**
     * Observable for network state during requests that the UI can monitor and
     * act based on state changes
     */
    val loadState: Flow<LoadState>

    /**
     * Performs the necessary operation to invoke a network retry request
     */
    suspend fun retryFailed()

    /**
     * Invalidate data source and, re-run the last successful or last failed request if applicable
     */
    suspend fun refresh()
}
