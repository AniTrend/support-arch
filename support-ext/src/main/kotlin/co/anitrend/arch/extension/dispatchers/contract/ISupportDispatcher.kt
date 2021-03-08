package co.anitrend.arch.extension.dispatchers.contract

import kotlinx.coroutines.CoroutineDispatcher

/**
 * @property main Context that is confined to the Main thread
 * @property computation Context that is used by all standard builders
 * @property io Context that is designed for offloading blocking IO tasks
 * @property confined Context that is confined to a single thread
 *
 * @since v1.3.0
 */
interface ISupportDispatcher {
    val main: CoroutineDispatcher
    val computation: CoroutineDispatcher
    val io: CoroutineDispatcher
    val confined: CoroutineDispatcher
}