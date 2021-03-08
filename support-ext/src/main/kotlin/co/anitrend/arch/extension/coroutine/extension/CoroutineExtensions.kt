package co.anitrend.arch.extension.coroutine.extension

import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

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