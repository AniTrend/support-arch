package co.anitrend.arch.extension.coroutine

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Contract for implementing coroutine scope preference on [SupervisorJob](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-supervisor-job.html)
 *
 * @since v1.1.0
 */
interface SupportCoroutine : CoroutineScope {

    /**
     * Requires an instance of [kotlinx.coroutines.Job] or [kotlinx.coroutines.SupervisorJob]
     */
    val supervisorJob: Job

    /**
     * Persistent context for the coroutine
     *
     * @return [kotlin.coroutines.CoroutineContext]
     */
    override val coroutineContext: CoroutineContext
        get() = supervisorJob + coroutineDispatcher

    /**
     * A failure or cancellation of a child does not cause the supervisor job
     * to fail and does not affect its other children.
     *
     * @return [kotlinx.coroutines.CoroutineScope]
     */
    val scope: CoroutineScope
        get() = CoroutineScope(coroutineContext)

    /**
     * Coroutine dispatcher specification
     *
     * @return [kotlinx.coroutines.Dispatchers.Default] by default
     */
    val coroutineDispatcher: CoroutineDispatcher
        get() = Dispatchers.Default

    /**
     * For more details regarding how cancellation is handled
     *
     * @see [kotlinx.coroutines.Job.cancelChildren]
     */
    fun cancelAllChildren() =
            scope.coroutineContext.cancelChildren()
}