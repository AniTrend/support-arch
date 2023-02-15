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
import androidx.lifecycle.whenStateAtLeast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Runs the given block when the [LifecycleOwner]'s [Lifecycle] is at least in
 * [Lifecycle.State.DESTROYED] state.
 *
 * @see Lifecycle.whenStateAtLeast for details
 */
suspend fun <T> LifecycleOwner.whenDestroyed(block: suspend CoroutineScope.() -> T): T =
    lifecycle.whenDestroyed(block)

/**
 * Runs the given block when the [Lifecycle] is at least in [Lifecycle.State.DESTROYED] state.
 *
 * @see Lifecycle.whenStateAtLeast for details
 */
suspend fun <T> Lifecycle.whenDestroyed(block: suspend CoroutineScope.() -> T): T {
    return whenStateAtLeast(Lifecycle.State.DESTROYED, block)
}

/**
 * Launches and runs the given block when the [Lifecycle] controlling this
 * [LifecycleCoroutineScope] is at least in [Lifecycle.State.DESTROYED] state.
 *
 * The returned [Job] will be cancelled when the [Lifecycle] is destroyed.
 * @see Lifecycle.whenDestroyed
 * @see Lifecycle.coroutineScope
 */
fun LifecycleCoroutineScope.launchWhenDestroyed(
    block: suspend CoroutineScope.() -> Unit,
): Job = launch {
    if (this is LifecycleOwner) {
        whenDestroyed(block)
    } else {
        error("${javaClass.simpleName} is not a LifecycleOwner")
    }
}
