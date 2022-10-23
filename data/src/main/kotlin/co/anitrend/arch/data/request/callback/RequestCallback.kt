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

package co.anitrend.arch.data.request.callback

import co.anitrend.arch.data.request.contract.IRequestHelper
import co.anitrend.arch.data.request.exception.RequestException
import co.anitrend.arch.data.request.wrapper.RequestWrapper
import co.anitrend.arch.domain.entities.RequestError
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A request callback for updating request status to the [helper]
 *
 * @param wrapper The wrapper that was executed
 * @param helper The helper to report to
 *
 * @since v1.3.0
 */
class RequestCallback(
    private val wrapper: RequestWrapper,
    private val helper: IRequestHelper
) {
    private val called = AtomicBoolean()

    /**
     * Call this method when the request succeeds and new data is fetched.
     *
     * @throws RequestError when [recordFailure]/[recordSuccess] has already been called
     */
    @Throws(RequestException::class)
    suspend fun recordSuccess() {
        if (called.compareAndSet(false, true))
            helper.recordResult(wrapper, null)
        else
            throw RequestException.ResultAlreadyRecorded(
                "already called recordSuccess or recordFailure"
            )
    }

    /**
     * Call this method with the failure message and the request can be retried via
     * [IRequestHelper.retryWithStatus].
     *
     * @param throwable The error that occurred while carrying out the request.
     *
     * @throws RequestError when [recordFailure]/[recordSuccess] has already been called
     */
    @Throws(RequestException::class)
    suspend fun recordFailure(throwable: RequestError) {
        if (called.compareAndSet(false, true))
            helper.recordResult(wrapper, throwable)
        else
            throw RequestException.ResultAlreadyRecorded(
                "already called recordSuccess or recordFailure"
            )
    }
}
