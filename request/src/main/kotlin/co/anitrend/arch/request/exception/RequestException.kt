/**
 * Copyright 2022 AniTrend
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

package co.anitrend.arch.request.exception

/**
 * An explicit request exception for any [co.anitrend.arch.request.callback.RequestCallback]
 * operations
 *
 * @param message Optional message regarding the exception
 * @param cause Optional exception for additional context
 *
 * @since v1.4.1-alpha02
 */
sealed class RequestException(
    message: String? = null,
    cause: Throwable? = null,
) : IllegalStateException(message, cause) {

    /**
     * An exception that represents a duplicate call to update a request status
     *
     * @param message Optional message regarding the exception
     * @param cause Optional exception for additional context
     *
     * @see RequestException
     * @since v1.4.1-alpha02
     */
    class ResultAlreadyRecorded(
        override val message: String? = null,
        override val cause: Throwable? = null,
    ) : RequestException()
}
