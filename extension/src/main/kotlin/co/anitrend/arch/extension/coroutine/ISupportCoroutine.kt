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

package co.anitrend.arch.extension.coroutine

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren

/**
 * Contract for implementing coroutine scope preference on [SupervisorJob](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-supervisor-job.html)
 *
 * @since v1.1.0
 */
interface ISupportCoroutine : CoroutineScope {

    /**
     * Requires an instance of [kotlinx.coroutines.Job] or [kotlinx.coroutines.SupervisorJob]
     */
    val supervisorJob: Job

    /**
     * Coroutine dispatcher specification
     *
     * @return one of the sub-types of [kotlinx.coroutines.Dispatchers]
     */
    val coroutineDispatcher: CoroutineDispatcher

    /**
     * Persistent context for the coroutine
     *
     * @return [kotlin.coroutines.CoroutineContext] preferably built from
     * [supervisorJob] + [coroutineDispatcher]
     */
    override val coroutineContext: CoroutineContext

    /**
     * A failure or cancellation of a child does not cause the supervisor job
     * to fail and does not affect its other children.
     *
     * @return [kotlinx.coroutines.CoroutineScope]
     */
    val scope: CoroutineScope

    /**
     * For more details regarding how cancellation is handled
     *
     * @see [kotlinx.coroutines.Job.cancelChildren]
     */
    fun cancelAllChildren() =
        scope.coroutineContext.cancelChildren()
}
