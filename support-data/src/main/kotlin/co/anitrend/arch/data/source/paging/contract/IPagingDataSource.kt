package co.anitrend.arch.data.source.paging.contract

import co.anitrend.arch.data.source.contract.IDataSource
import co.anitrend.arch.extension.SupportDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Contract for abstract paging data source implementation
 *
 * @since v1.1.0
 */
interface IPagingDataSource : IDataSource {

    /**
     * Clears data sources (databases, preferences, e.t.c)
     */
    suspend fun clearDataSource()

    /**
     * Invokes [clearDataSource] and should invoke network refresh or reload
     */
    fun invalidateAndRefresh() {
        launch(Dispatchers.IO) {
            clearDataSource()
        }
    }

    /**
     * Performs the necessary operation to invoke a network retry request
     */
    fun retryRequest()
}