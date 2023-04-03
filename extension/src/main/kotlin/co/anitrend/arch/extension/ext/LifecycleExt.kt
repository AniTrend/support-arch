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

package co.anitrend.arch.extension.ext

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Launches a coroutine for [block] when the [LifecycleOwner] matches [state]
 *
 * @see Lifecycle.repeatOn
 */
suspend fun LifecycleOwner.repeatOn(
    state: Lifecycle.State,
    block:
    suspend CoroutineScope.() -> Unit,
) = lifecycle.repeatOn(state, block)

/**
 * Launches a coroutine for [block] when the [Lifecycle] matches [state]
 *
 * @see LifecycleCoroutineScope.repeatOn
 */
suspend fun Lifecycle.repeatOn(
    state: Lifecycle.State,
    block: suspend CoroutineScope.() -> Unit,
) = repeatOnLifecycle(state, block)

/**
 * Launches and runs the given block when the [Lifecycle] controlling this
 * [LifecycleCoroutineScope] is goes into the the matching state.
 *
 * @param state Lifecycle state to repeat on
 * @param block The block of suspend code to invoke
 *
 * @return [Job] of the launching scope
 *
 * @see Lifecycle.repeatOn
 */
fun LifecycleCoroutineScope.repeatOn(
    state: Lifecycle.State,
    block: suspend CoroutineScope.() -> Unit,
): Job = launch {
    if (this is LifecycleOwner) {
        repeatOn(state, block)
    } else {
        error("${javaClass.simpleName} is not a LifecycleOwner")
    }
}
