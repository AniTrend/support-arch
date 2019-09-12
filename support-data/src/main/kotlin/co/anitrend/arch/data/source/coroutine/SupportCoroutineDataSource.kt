package co.anitrend.arch.data.source.coroutine

import androidx.lifecycle.MutableLiveData
import co.anitrend.arch.data.source.coroutine.contract.ICoroutineDataSource
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.util.SupportConnectivityHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * A coroutine that returns [NetworkState] to inform the caller about it's progress.
 * This data source is targeted for non-UI components
 *
 * @since v1.1.0
 */
abstract class SupportCoroutineDataSource<P, R> : ICoroutineDataSource<P, R>, KoinComponent {

    protected val moduleTag: String = javaClass.simpleName

    /**
     * Connectivity helper utility with live data observable capabilities
     */
    protected val connectivityHelper by inject<SupportConnectivityHelper>()

    /**
     * Requires an instance of [kotlinx.coroutines.Job] or [kotlinx.coroutines.SupervisorJob]
     */
    override val supervisorJob: Job = SupervisorJob()

    override val networkState = MutableLiveData<NetworkState>()

    /**
     * Function reference for the retry event
     */
    protected var retry: (suspend () -> R)? = null

    private suspend fun retryPreviousRequest() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
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