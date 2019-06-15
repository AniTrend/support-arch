package io.wax911.support.data.source.coroutine.contract

import io.wax911.support.data.model.NetworkState
import io.wax911.support.data.source.contract.IDataSource

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