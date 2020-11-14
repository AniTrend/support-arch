package co.anitrend.arch.extension.dispatchers

import kotlinx.coroutines.*

/**
 * @param main Context that is confined to the Main thread operating with UI objects
 * @param computation Context that is used by all standard builders, It is backed by a
 * shared pool of threads on JVM. By default, the maximal level of parallelism used
 * by this dispatcher is equal to the number of CPU cores, but is at least two.
 * @param io Context that is designed for offloading blocking IO tasks to a shared pool of threads.
 * @param confined Context that is confined to a single thread
 *
 * @since v1.2.0
 */
data class SupportDispatchers constructor(
    val main: MainCoroutineDispatcher = Dispatchers.Main,
    val computation: CoroutineDispatcher = Dispatchers.Default,
    val io: CoroutineDispatcher = Dispatchers.IO,
    @OptIn(ObsoleteCoroutinesApi::class)
    val confined: ExecutorCoroutineDispatcher = newSingleThreadContext("ConfinedContext")
)