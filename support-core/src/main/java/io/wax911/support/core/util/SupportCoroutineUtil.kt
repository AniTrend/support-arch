package io.wax911.support.core.util

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

interface SupportCoroutineUtil : CoroutineScope {

    private val job: Job
        get() = SupervisorJob()

    /**
     *
     * @return [kotlin.coroutines.CoroutineContext]
     */
    override val coroutineContext: CoroutineContext
        get() = job + coroutineDispatcher

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