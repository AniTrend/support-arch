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

package co.anitrend.arch.request.wrapper

import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.arch.request.contract.IRequestHelper
import co.anitrend.arch.request.model.Request

/**
 * A wrapper for creating requests
 *
 * @param handleCallback The callback that should be invoked
 * @param helper A type of request helper to use
 * @param request The type of request
 *
 * @since v1.3.0
 */
class RequestWrapper internal constructor(
    val handleCallback: suspend (RequestCallback) -> Unit,
    val helper: IRequestHelper,
    val request: Request,
) {
    /**
     * Retries a request if it is not already running
     */
    suspend operator fun invoke() {
        handleCallback(
            RequestCallback(
                wrapper = this,
                helper = helper,
            ),
        )
    }

    /**
     * Retries a request if it is not already running
     */
    suspend fun retry() {
        helper.runIfNotRunning(request, handleCallback)
    }
}
