package co.anitrend.arch.extension.ext

import androidx.lifecycle.*
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
    block: suspend CoroutineScope.() -> Unit
): Job = launch {
    if (this is LifecycleOwner)
        whenDestroyed(block)
    else
        error("${javaClass.simpleName} is not a LifecycleOwner")
}