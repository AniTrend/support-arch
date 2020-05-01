package co.anitrend.arch.data.source.coroutine.contract

import co.anitrend.arch.data.source.contract.IDataSource
import co.anitrend.arch.domain.entities.NetworkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Contract for coroutine data source abstraction or implementation
 *
 * @since v1.1.0
 */
interface ICoroutineDataSource<P, R> : IDataSource {

    /**
     * Clears data sources (databases, preferences, e.t.c)
     */
    suspend fun clearDataSource()

    /**
     * Invokes [clearDataSource] and should invoke network refresh or reload
     */
    suspend fun invalidateAndRefresh()

    /**
     * Performs the necessary operation to invoke a network retry request
     */
    suspend fun retryRequest()
}