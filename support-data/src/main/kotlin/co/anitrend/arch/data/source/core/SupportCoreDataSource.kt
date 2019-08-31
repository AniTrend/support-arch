package co.anitrend.arch.data.source.core

import androidx.lifecycle.MutableLiveData
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.data.source.core.contract.ICoreDataSource
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
 * @param parentCoroutineJob parent coroutine from something that is lifecycle aware,
 * this enables us to cancels jobs automatically when the parent is also canceled
 */
abstract class SupportCoreDataSource(
    parentCoroutineJob: Job? = null
) : ICoreDataSource, KoinComponent {

    protected val moduleTag: String = javaClass.simpleName

    /**
     * Connectivity helper utility with live data observable capabilities
     */
    protected val connectivityHelper by inject<SupportConnectivityHelper>()

    /**
     * Requires an instance of [kotlinx.coroutines.Job] or [kotlinx.coroutines.SupervisorJob]
     */
    override val supervisorJob: Job = SupervisorJob(parentCoroutineJob)

    override val networkState = MutableLiveData<co.anitrend.arch.domain.entities.NetworkState>()

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
     * Dispatches work for the paging data source to respective workers or mappers
     * that publish the result to any [androidx.lifecycle.LiveData] observers
     *
     * @see networkState
     */
    override fun invoke() {
        networkState.postValue(co.anitrend.arch.domain.entities.NetworkState.Success)
        retry = { invoke() }
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