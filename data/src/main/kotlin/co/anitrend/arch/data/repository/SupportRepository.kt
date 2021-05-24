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

package co.anitrend.arch.data.repository

import co.anitrend.arch.data.repository.contract.ISupportRepository
import co.anitrend.arch.extension.coroutine.ISupportCoroutine

/**
 * Core repository implementation with data source cancellation support
 *
 * @param source Data source that implements coroutine behaviour
 *
 * @since v1.1.0
 */
abstract class SupportRepository(
    private val source: ISupportCoroutine?
) : ISupportRepository {

    /**
     * Module tag for the current context
     */
    protected val moduleTag: String = javaClass.simpleName

    /**
     * Deals with cancellation of any pending or on going operations that the repository
     * might be working on
     */
    override fun onCleared() {
        source?.cancelAllChildren()
    }
}
