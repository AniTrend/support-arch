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
 * State representing ongoing, completed or failed operations
 *
 * @property position Where the loader should be placed
 */
sealed class LoadState {

    abstract val position: Position

    /**
     * State position identifier
     */
    enum class Position { TOP, BOTTOM, UNDEFINED }

    /**
     * Load state is idle
     */
    data class Idle(
        override val position: Position = Position.UNDEFINED,
    ) : LoadState()

    /**
     * Load state is successful
     */
    data class Success(
        override val position: Position = Position.UNDEFINED,
    ) : LoadState()

    /**
     * Load state is loading
     */
    data class Loading(
        override val position: Position = Position.UNDEFINED,
    ) : LoadState()

    /**
     * Load state for failed loading
     *
     * @param details General [Throwable] type with details regarding the error
     */
    data class Error(
        val details: Throwable,
        override val position: Position = Position.UNDEFINED,
    ) : LoadState()
}
