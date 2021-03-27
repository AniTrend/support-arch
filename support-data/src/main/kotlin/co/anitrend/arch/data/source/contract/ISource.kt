package co.anitrend.arch.data.source.contract

import kotlinx.coroutines.CoroutineDispatcher

interface ISource {

    /**
     * Informs the data source to invalidate itself and should invoke network refresh or reload
     */
    suspend fun invalidate()

    /**
     * Clears data sources (databases, preferences, e.t.c)
     *
     * @param context Dispatcher context to run in
     */
    suspend fun clearDataSource(context: CoroutineDispatcher)
}