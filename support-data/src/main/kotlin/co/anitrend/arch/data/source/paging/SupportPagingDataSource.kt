package co.anitrend.arch.data.source.paging

import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import androidx.paging.extension.createStatusLiveData
import co.anitrend.arch.data.source.paging.contract.IPagingDataSource
import co.anitrend.arch.extension.SupportDispatchers
import co.anitrend.arch.extension.util.SupportExtKeyStore
import co.anitrend.arch.extension.util.pagination.SupportPagingHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asExecutor

/**
 * A non-coroutine that depends on [androidx.lifecycle.LiveData] to publish results.
 * This data source is targeted for UI components that depend on [androidx.paging.PagedList]
 *
 * @since v1.1.0
 */
abstract class SupportPagingDataSource<T>(
    protected val dispatchers: SupportDispatchers
) : PagedList.BoundaryCallback<T>(), IPagingDataSource {

    protected val moduleTag: String = javaClass.simpleName

    /**
     * Connectivity helper utility with live data observable capabilities
     */
    protected val connectivityHelper by inject<SupportConnectivity>()

    /**
     * Requires an instance of [kotlinx.coroutines.Job] or [kotlinx.coroutines.SupervisorJob]
     */
    final override val supervisorJob = SupervisorJob()

    protected val pagingRequestHelper by lazy(LAZY_MODE_UNSAFE) {
        PagingRequestHelper(coroutineDispatcher.asExecutor())
    }

    override val networkState by lazy(LAZY_MODE_UNSAFE) {
        pagingRequestHelper.createStatusLiveData()
    }

    protected val supportPagingHelper = SupportPagingHelper(
        isPagingLimit = false,
        pageSize = SupportExtKeyStore.pagingLimit
    )

    /**
     * Invokes [clearDataSource] and should invoke network refresh or reload
     */
    override fun invalidateAndRefresh() {
        super.invalidateAndRefresh()
        supportPagingHelper.onPageRefresh()
    }

    /**
     * Performs the necessary operation to invoke a network retry request
     */
    override fun retryRequest() {
        pagingRequestHelper.retryAllFailed()
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