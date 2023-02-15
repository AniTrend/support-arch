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

package co.anitrend.arch.data.request.report

import co.anitrend.arch.data.request.model.Request
import co.anitrend.arch.data.request.report.contract.IRequestStatusReport
import co.anitrend.arch.domain.entities.RequestError

/**
 * Data class that holds the information about the current status of the ongoing requests
 * using this helper.
 */
data class RequestStatusReport internal constructor(
    private val request: Request,
) : IRequestStatusReport {

    /**
     * Convenience method to check if there are any running requests.
     *
     * @return True if there are any running requests, false otherwise.
     */
    override fun hasRunning(): Boolean {
        return request.status == Request.Status.RUNNING
    }

    /**
     * Convenience method to check if there are any requests that resulted in an error.
     *
     * @return True if there are any requests that finished with error, false otherwise.
     */
    override fun hasError(): Boolean {
        return request.status == Request.Status.FAILED
    }

    /**
     * Convenience method to check if there are any idle requests.
     *
     * @return True if there are no requests.
     */
    override fun hasIdle(): Boolean {
        return request.status == Request.Status.IDLE
    }

    /**
     * Convenience method to check if there are any requests that resulted in success.
     *
     * @return True if there are any requests that finished without an error, false otherwise.
     */
    override fun hasSuccess(): Boolean {
        return request.status == Request.Status.SUCCESS
    }

    /**
     * Returns the error for the given request type.
     *
     * @param type The request type for which the error should be returned.
     *
     * @return The [Throwable] returned by the failing request with the given type or
     * `null` if the request for the given type did not fail.
     */
    override fun getErrorFor(type: Request.Type): RequestError? {
        if (request.type == type) {
            return request.lastError
        }
        return null
    }

    /**
     * Returns the current request type.
     *
     * @return The [Request.Type] in the current context
     */
    override fun getType(): Request.Type = request.type

    override fun toString(): String {
        return """
                StatusReport {
                    request: $request
                }
        """.trimIndent()
    }
}
