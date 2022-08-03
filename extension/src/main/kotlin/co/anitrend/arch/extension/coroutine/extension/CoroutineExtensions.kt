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

package co.anitrend.arch.extension.coroutine.extension

import androidx.annotation.VisibleForTesting
import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob

@VisibleForTesting
internal class Coroutine(
    override val supervisorJob: Job,
    override val coroutineDispatcher: CoroutineDispatcher
) : ISupportCoroutine {
    /**
     * Persistent context for the coroutine
     *
     * @return [kotlin.coroutines.CoroutineContext] preferably built from
     * [supervisorJob] + [coroutineDispatcher]
     */
    override val coroutineContext: CoroutineContext = supervisorJob + coroutineDispatcher

    /**
     * A failure or cancellation of a child does not cause the supervisor job
     * to fail and does not affect its other children.
     *
     * @return [kotlinx.coroutines.CoroutineScope]
     */
    override val scope: CoroutineScope = CoroutineScope(coroutineContext)
}

@Suppress("FunctionName")
fun Default(): ISupportCoroutine =
    Coroutine(SupervisorJob(), Dispatchers.Default)

@Suppress("FunctionName")
fun Main(): ISupportCoroutine =
    Coroutine(SupervisorJob(), Dispatchers.Main)

@Suppress("FunctionName")
fun Io(): ISupportCoroutine =
    Coroutine(SupervisorJob(), Dispatchers.IO)
