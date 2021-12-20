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

@Deprecated(
    "Replace with LoadState",
    level = DeprecationLevel.WARNING,
    replaceWith = ReplaceWith(
        "LoadState",
        "co.anitrend.arch.domain.entities.LoadState"
    )
)
/**
 * State representing ongoing, completed or failed requests
 */
sealed class NetworkState {

    /**
     * Represents a state of idle
     */
    @Suppress("DEPRECATION")
    object Idle : NetworkState()

    /**
     * Represents a network state of loading
     */
    @Suppress("DEPRECATION")
    object Loading : NetworkState()

    /**
     * Represents a network state that has succeeded
     */
    @Suppress("DEPRECATION")
    object Success : NetworkState()

    /**
     * Network state for failed requests with an optional message or code
     *
     * @param code The response code from the last request
     * @param heading Heading related to the error message
     * @param message Message to display if any are available
     */
    @Suppress("DEPRECATION")
    data class Error(
        val code: Int? = null,
        val heading: String? = null,
        val message: String? = null
    ) : NetworkState()
}
