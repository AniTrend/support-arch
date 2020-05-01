package co.anitrend.arch.data.source.coroutine

import androidx.lifecycle.MutableLiveData
import co.anitrend.arch.data.source.coroutine.contract.ICoroutineDataSource
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.SupportDispatchers
import co.anitrend.arch.extension.network.SupportConnectivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * A coroutine that returns [NetworkState] to inform the caller about it's progress.
 * This data source is targeted for non-UI components
 *
 * @since v1.1.0
 */
abstract class SupportCoroutineDataSource<P, R>(
    protected val dispatchers: SupportDispatchers
) : ICoroutineDataSource<P, R>, KoinComponent {

    protected val moduleTag: String = javaClass.simpleName

    /**
     * Requires an instance of [kotlinx.coroutines.Job] or [kotlinx.coroutines.SupervisorJob]
     */
    final override val supervisorJob: Job = SupervisorJob()

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
        withContext(dispatchers.io) {
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