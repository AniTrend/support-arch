package co.anitrend.arch.data.source.core

import androidx.lifecycle.MutableLiveData
import co.anitrend.arch.data.source.core.contract.ICoreDataSource
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * A non-coroutine that depends on [androidx.lifecycle.LiveData] to publish results.
 * This data source is targeted for UI components, but not for [androidx.paging.PagedList]
 * dependant resources that may require boundary callbacks.
 *
 * @param dispatchers Dispatchers that are currently available
 *
 * @since v1.1.0
 */
abstract class SupportCoreDataSource(
    protected val dispatchers: SupportDispatchers
) : ICoreDataSource {

    /**
     * Module tag for the current context
     */
    protected val moduleTag: String = javaClass.simpleName

    /**
     * Requires an instance of [kotlinx.coroutines.Job] or [kotlinx.coroutines.SupervisorJob]
     */
    final override val supervisorJob: Job = SupervisorJob()

    /**
     * Observable for network state during requests that the UI can monitor and
     * act based on state changes
     */
    override val networkState =
        MutableStateFlow<NetworkState>(NetworkState.Idle)

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
        launch(dispatchers.io) {
            clearDataSource()
        }
        retryPreviousRequest()
    }

    /**
     * Coroutine dispatcher specification
     *
     * @return one of the sub-types of [kotlinx.coroutines.Dispatchers]
     */
    final override val coroutineDispatcher = dispatchers.computation

    /**
     * Persistent context for the coroutine
     *
     * @return [kotlin.coroutines.CoroutineContext] preferably built from
     * [supervisorJob] + [coroutineDispatcher]
     */
    final override val coroutineContext = supervisorJob + coroutineDispatcher

    /**
     * A failure or cancellation of a child does not cause the supervisor job
     * to fail and does not affect its other children.
     *
     * @return [kotlinx.coroutines.CoroutineScope]
     */
    final override val scope = CoroutineScope(coroutineContext)
}