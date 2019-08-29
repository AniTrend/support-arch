package io.wax911.support.data.source.paging

import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import androidx.paging.extension.createStatusLiveData
import io.wax911.support.data.source.paging.contract.IPagingDataSource
import io.wax911.support.data.util.pagination.SupportPagingHelper
import io.wax911.support.extension.util.SupportConnectivityHelper
import io.wax911.support.extension.util.SupportExtKeyStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * A non-coroutine that depends on [androidx.lifecycle.LiveData] to publish results.
 * This data source is targeted for UI components that depend on [androidx.paging.PagedList]
 *
 * @param parentCoroutineJob parent coroutine from something that is lifecycle aware,
 * this enables us to cancels jobs automatically when the parent is also canceled
 */
abstract class SupportPagingDataSource<T>(
    parentCoroutineJob: Job? = null
) : PagedList.BoundaryCallback<T>(), IPagingDataSource, KoinComponent {

    protected val moduleTag: String = javaClass.simpleName

    /**
     * Connectivity helper utility with live data observable capabilities
     */
    protected val connectivityHelper by inject<SupportConnectivityHelper>()

    /**
     * Requires an instance of [kotlinx.coroutines.Job] or [kotlinx.coroutines.SupervisorJob]
     */
    override val supervisorJob = SupervisorJob(parentCoroutineJob)

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