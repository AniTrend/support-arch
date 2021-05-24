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

package co.anitrend.arch.domain.entities

/**
 * Custom error for requests
 *
 * @param topic Summary of the error
 * @param description Description of the error
 * @param throwable Full stack cause of the error
 *
 * @since v1.3.0
 */
data class RequestError(
    val topic: String? = null,
    val description: String? = null,
    val throwable: Throwable? = null
) : Throwable(description, throwable) {
    constructor(cause: Throwable?) : this(
        cause?.javaClass?.simpleName,
        cause?.toString(),
        cause
    )
}
