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

package co.anitrend.arch.paging.legacy.source

import co.anitrend.arch.extension.util.DEFAULT_PAGE_SIZE
import co.anitrend.arch.extension.util.pagination.SupportPagingHelper
import co.anitrend.arch.paging.legacy.source.contract.AbstractPagingDataSource
import co.anitrend.arch.request.model.Request

/**
 * A data source that is targeted for [androidx.paging.PagedList]
 *
 * @param dispatcher Dispatchers that are currently available
 *
 * @since v1.1.0
 */
abstract class SupportPagingDataSource<T> : AbstractPagingDataSource<T>() {
    /**
     * Representation of the paging state
     */
    protected open val supportPagingHelper =
        SupportPagingHelper(
            isPagingLimit = false,
            pageSize = DEFAULT_PAGE_SIZE,
        )

    /**
     * Invokes [clearDataSource] and should invoke network refresh or reload
     */
    override suspend fun invalidate() {
        clearDataSource(dispatcher.io)
        supportPagingHelper.onPageRefresh()
    }

    /**
     * Performs the necessary operation to invoke a network retry request
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
        invalidate()
    }
}
