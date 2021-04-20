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

package co.anitrend.arch.data.request.contract

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.model.Request
import co.anitrend.arch.data.request.report.contract.IRequestStatusReport
import co.anitrend.arch.data.request.wrapper.RequestWrapper
import co.anitrend.arch.domain.entities.RequestError

/**
 * Request helper contract
 *
 * @since v1.3.0
 */
interface IRequestHelper {

    /**
     * Adds a new listener that will be notified when any request changes [Request.Status].
     *
     * @param listener The listener that will be notified each time a request's status changes.
     * @return True if it is added, false otherwise (e.g. it already exists in the list).
     */
    fun addListener(listener: Listener): Boolean

    /**
     * Removes the given listener from the listeners list.
     *
     * @param listener The listener that will be removed.
     * @return True if the listener is removed, false otherwise (e.g. it never existed)
     */
    fun removeListener(listener: Listener): Boolean

    /**
     * Runs the given [RequestCallback] if no other requests in the given request type is already
     * running. If run, the request will be run in the current thread.
     *
     * @param request The type of the request.
     * @param handleCallback The request to run.
     *
     * @return True if the request is run, false otherwise.
     */
    suspend fun runIfNotRunning(
        request: Request,
        handleCallback: suspend (RequestCallback) -> Unit
    ): Boolean

    /**
     * Records the result of the result of a request
     *
     * @param wrapper Request wrapper delegate
     * @param throwable An optional error
     */
    suspend fun recordResult(
        wrapper: RequestWrapper,
        throwable: RequestError?
    )

    /**
     * Retries all request types for a given [status].
     *
     * @param status Status for request to retry
     * @param action Action to run when [status is satisfied]
     *
     * @return True if any request is retried, false otherwise.
     */
    suspend fun retryWithStatus(
        status: Request.Status = Request.Status.FAILED,
        action: suspend () -> Unit
    ): Boolean

    /**
     * Check if request handler has any finished request with [status]
     *
     * @return True if a match is found, false otherwise.
     */
    suspend fun hasAnyWithStatus(status: Request.Status): Boolean

    /**
     * Listener interface to get notified by request status changes.
     */
    interface Listener {
        /**
         * Called when the status for any of the requests has changed.
         *
         * @param report The current status report that has all the information about the requests.
         */
        fun onStatusChange(report: IRequestStatusReport)
    }
}
