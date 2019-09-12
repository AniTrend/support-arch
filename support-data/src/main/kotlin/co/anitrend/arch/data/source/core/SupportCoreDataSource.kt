package co.anitrend.arch.data.source.core

import androidx.lifecycle.MutableLiveData
import co.anitrend.arch.data.source.core.contract.ICoreDataSource
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.util.SupportConnectivityHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * A non-coroutine that depends on [androidx.lifecycle.LiveData] to publish results.
 * This data source is targeted for UI components, but not for [androidx.paging.PagedList]
 * dependant resources that may require boundary callbacks.
 *
 * @since v1.1.0
 */
abstract class SupportCoreDataSource<P> : ICoreDataSource, KoinComponent {

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
    protected var retry: (() -> Unit)? = null

    private fun retryPreviousRequest() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }

    /**
     * Retries the last executed request
     */
    override fun retryRequest() {
        retryPreviousRequest()
    }

    /**
     * Invokes [clearDataSource] and should invoke network refresh or reload
     */
    override fun invalidateAndRefresh() {
        super.invalidateAndRefresh()
        retryPreviousRequest()
    }
}