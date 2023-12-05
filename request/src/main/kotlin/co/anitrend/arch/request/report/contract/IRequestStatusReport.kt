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

package co.anitrend.arch.request.report.contract

import co.anitrend.arch.domain.entities.RequestError
import co.anitrend.arch.request.model.Request

/**
 * Status report contract
 */
interface IRequestStatusReport {
    /**
     * Convenience method to check if there are any running requests.
     *
     * @return True if there are any running requests, false otherwise.
     */
    fun hasRunning(): Boolean

    /**
     * Convenience method to check if there are any requests that resulted in an error.
     *
     * @return True if there are any requests that finished with error, false otherwise.
     */
    fun hasError(): Boolean

    /**
     * Convenience method to check if there are any idle requests.
     *
     * @return True if there are no requests.
     */
    fun hasIdle(): Boolean

    /**
     * Convenience method to check if there are any requests that resulted in success.
     *
     * @return True if there are any requests that finished without an error, false otherwise.
     */
    fun hasSuccess(): Boolean

    /**
     * Returns the error for the given request type.
     *
     * @param type The request type for which the error should be returned.
     *
     * @return The [Throwable] returned by the failing request with the given type or
     * `null` if the request for the given type did not fail.
     */
    fun getErrorFor(type: Request.Type): RequestError?

    /**
     * Returns the current request type.
     *
     * @return The [Request.Type] in the current context
     */
    fun getType(): Request.Type
}
