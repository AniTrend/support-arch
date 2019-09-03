package co.anitrend.arch.data.source.paging

import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import androidx.paging.extension.createStatusLiveData
import co.anitrend.arch.data.source.paging.contract.IPagingDataSource
import co.anitrend.arch.extension.util.SupportConnectivityHelper
import co.anitrend.arch.extension.util.SupportExtKeyStore
import co.anitrend.arch.extension.util.pagination.SupportPagingHelper
import kotlinx.coroutines.SupervisorJob
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

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
    protected val connectivityHelper by inject<SupportConnectivityHelper>()

    /**
     * Requires an instance of [kotlinx.coroutines.Job] or [kotlinx.coroutines.SupervisorJob]
     */
    override val supervisorJob = SupervisorJob()

    protected val pagingRequestHelper = PagingRequestHelper(IO_EXECUTOR)

    override val networkState = pagingRequestHelper.createStatusLiveData()

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

    companion object {
        val IO_EXECUTOR: ExecutorService = Executors.newSingleThreadExecutor()
    }
}