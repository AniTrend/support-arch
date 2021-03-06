package co.anitrend.arch.data.source.contract

import co.anitrend.arch.data.request.AbstractRequestHelper
import co.anitrend.arch.domain.entities.NetworkState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IDataSource {

    /**
     * Request helper that controls the flow of requests to the implementing data source to avoid
     * multiple requests of the same type before others are completed for this instance
     *
     * @see AbstractRequestHelper
     */
    val requestHelper: AbstractRequestHelper

    /**
     * Observable for network state during requests that the UI can monitor and
     * act based on state changes
     */
    val networkState: Flow<NetworkState>

    /**
     * Performs the necessary operation to invoke a network retry request
     */
    suspend fun retryFailed()

    /**
     * Invalidate data source and, re-run the last successful or last failed request if applicable
     */
    suspend fun refresh()

    /**
     * Clears data sources (databases, preferences, e.t.c)
     *
     * @param context Dispatcher context to run in
     */
    suspend fun clearDataSource(context: CoroutineDispatcher)
}