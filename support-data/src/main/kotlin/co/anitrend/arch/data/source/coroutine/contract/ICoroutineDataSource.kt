package co.anitrend.arch.data.source.coroutine.contract

import co.anitrend.arch.data.source.contract.IDataSource
import co.anitrend.arch.domain.entities.NetworkState

/**
 * Contract for coroutine data source abstraction or implementation
 *
 * @since v1.1.0
 */
interface ICoroutineDataSource : IDataSource {

    /**
     * Handles the requesting data from a the network source and returns
     * [NetworkState] to the caller after execution
     */
    suspend operator fun invoke(): NetworkState

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