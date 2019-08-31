package co.anitrend.arch.data.source.coroutine.contract

import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.data.source.contract.IDataSource

/**
 *
 *
 * @since v1.1.0
 */
interface ICoroutineDataSource : IDataSource {

    /**
     * Handles the requesting data from a the network source and returns
     * [NetworkState] to the caller after execution
     */
    suspend operator fun invoke(): co.anitrend.arch.domain.entities.NetworkState

    /**
     * Clears data sources (databases, preferences, e.t.c)
     */
    suspend fun clearDataSource()

    /**
     * Invokes [clearDataSource] and should invoke network refresh or reload
     */
    suspend fun invalidateAndRefresh() {
        clearDataSource()
    }

    /**
     * Performs the necessary operation to invoke a network retry request
     */
    suspend fun retryRequest()
}