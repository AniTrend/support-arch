package co.anitrend.arch.data.source.core.contract

import co.anitrend.arch.data.source.contract.IDataSource
import kotlinx.coroutines.launch

/**
 * Contract for core data source abstraction
 *
 * @since v1.1.0
 */
interface ICoreDataSource : IDataSource {


    /**
     * Clears data sources (databases, preferences, e.t.c)
     */
    suspend fun clearDataSource()

    /**
     * Invokes [clearDataSource] and should invoke network refresh or reload
     */
    fun invalidateAndRefresh() {
        launch {
            clearDataSource()
        }
    }

    /**
     * Performs the necessary operation to invoke a network retry request
     */
    fun retryRequest()
}