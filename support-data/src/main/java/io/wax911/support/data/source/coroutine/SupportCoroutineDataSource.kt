package io.wax911.support.data.source.coroutine

import androidx.lifecycle.MutableLiveData
import io.wax911.support.data.model.NetworkState
import io.wax911.support.data.source.coroutine.contract.ICoroutineDataSource
import io.wax911.support.extension.util.SupportConnectivityHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * A coroutine that returns [io.wax911.support.data.model.NetworkState] to inform the caller about it's progress.
 * This data source is targeted for non-UI components
 *
 * @param parentCoroutineJob parent coroutine from something that is lifecycle aware,
 * this enables us to cancels jobs automatically when the parent is also canceled
 */
abstract class SupportCoroutineDataSource(
    parentCoroutineJob: Job? = null
) : ICoroutineDataSource, KoinComponent {

    protected val moduleTag: String = javaClass.simpleName

    /**
     * Connectivity helper utility with live data observable capabilities
     */
    protected val connectivityHelper by inject<SupportConnectivityHelper>()

    /**
     * Requires an instance of [kotlinx.coroutines.Job] or [kotlinx.coroutines.SupervisorJob]
     */
    override val supervisorJob: Job = SupervisorJob(parentCoroutineJob)

    override val networkState = MutableLiveData<NetworkState>()

    /**
     * Function reference for the retry event
     */
    protected var retry: (suspend () -> NetworkState)? = null

    private suspend fun retryPreviousRequest() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }

    /**
     * Handles the requesting data from a the network source and return
     * [NetworkState] to the caller after execution.
     *
     * In this context the super.invoke() method will allow a retry action to be set
     */
    override suspend fun invoke(): NetworkState {
        networkState.postValue(NetworkState.Loading)
        retry = { invoke() }
        return NetworkState.Loading
    }

    /**
     * Retries the last executed request
     */
    override suspend fun retryRequest() {
        retryPreviousRequest()
    }

    /**
     * Invokes [clearDataSource] and should invoke network refresh or reload
     */
    override suspend fun invalidateAndRefresh() {
        super.invalidateAndRefresh()
        retryPreviousRequest()
    }
}