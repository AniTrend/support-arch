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

package co.anitrend.arch.paging.legacy.source.contract

import androidx.paging.PagedList
import co.anitrend.arch.data.source.contract.IDataSource
import co.anitrend.arch.data.source.contract.ISource
import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.arch.extension.coroutine.extension.Default
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.request.AbstractRequestHelper
import co.anitrend.arch.request.extension.createStatusFlow
import co.anitrend.arch.request.helper.RequestHelper

abstract class AbstractPagingDataSource<T> :
    PagedList.BoundaryCallback<T>(),
    IDataSource,
    ISource,
    ISupportCoroutine by Default() {

    /**
     * Contract for multiple types of [coroutineDispatcher]
     */
    protected abstract val dispatcher: ISupportDispatcher

    /**
     * Request helper that controls the flow of requests to the implementing data source to avoid
     * multiple requests of the same type before others are completed for this instance
     *
     * @see AbstractRequestHelper
     */
    override val requestHelper by lazy {
        RequestHelper(dispatcher)
    }

    /**
     * Observable for network state during requests that the UI can monitor and
     * act based on state changes
     */
    override val loadState by lazy {
        requestHelper.createStatusFlow()
    }
}
