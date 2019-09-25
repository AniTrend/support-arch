package co.anitrend.arch.data.source.paging

import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import androidx.paging.extension.createStatusLiveData
import co.anitrend.arch.data.source.paging.contract.IPagingDataSource
import co.anitrend.arch.extension.LAZY_MODE_UNSAFE
import co.anitrend.arch.extension.network.SupportConnectivity
import co.anitrend.arch.extension.util.SupportExtKeyStore
import co.anitrend.arch.extension.util.pagination.SupportPagingHelper
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asExecutor
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * A non-coroutine that depends on [androidx.lifecycle.LiveData] to publish results.
 * This data source is targeted for UI components that depend on [androidx.paging.PagedList]
 *
 * @since v1.1.0
 */
abstract class SupportPagingDataSource<T> : PagedList.BoundaryCallback<T>(), IPagingDataSource, KoinComponent {

    protected val moduleTag: String = javaClass.simpleName

    /**
     * Connectivity helper utility with live data observable capabilities
     */
    protected val connectivityHelper by inject<SupportConnectivity>()

    /**
     * Requires an instance of [kotlinx.coroutines.Job] or [kotlinx.coroutines.SupervisorJob]
     */
    override val supervisorJob = SupervisorJob()

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
}