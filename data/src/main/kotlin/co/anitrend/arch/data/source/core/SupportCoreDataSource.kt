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

package co.anitrend.arch.data.source.core

import co.anitrend.arch.data.request.model.Request
import co.anitrend.arch.data.source.core.contract.AbstractDataSource

/**
 * A data source that depends on [kotlinx.coroutines.flow.Flow] to publish results.
 *
 * @param dispatcher Dispatchers that are currently available
 *
 * @since v1.1.0
 */
abstract class SupportCoreDataSource : AbstractDataSource() {

    /**
     * Invokes [clearDataSource] and should invoke network refresh or reload
     */
    override suspend fun invalidate() {
        clearDataSource(dispatcher.io)
    }

    /**
     * Retries the last executed request, may also be called in [refresh]
     *
     * @see refresh
     */
    override suspend fun retryFailed() {
        requestHelper.retryWithStatus(
            Request.Status.FAILED,
        ) {}
    }

    /**
     * Invalidate data source and, re-run the last successful or last failed request if applicable
     */
    override suspend fun refresh() {
        val ran = requestHelper.retryWithStatus(
            Request.Status.SUCCESS,
        ) { invalidate() }

        if (!ran) {
            retryFailed()
        }
    }
}
