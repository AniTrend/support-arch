package io.wax911.support.data.source

import android.os.Bundle
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import androidx.paging.extension.createStatusLiveData
import androidx.room.RoomDatabase
import io.wax911.support.data.source.contract.ISupportDataSource
import io.wax911.support.data.util.pagination.SupportPagingHelper
import io.wax911.support.extension.util.SupportConnectivityHelper
import io.wax911.support.extension.util.SupportExtKeyStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import org.koin.core.inject
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * A paging dependant data source for list resource requests
 *
 * @param parentCoroutineJob parent coroutine from something that is lifecycle aware,
 * this enables us to cancels jobs automatically when the parent is also canceled
 */
abstract class SupportPagingDataSource<T>(
    protected val bundle: Bundle,
    parentCoroutineJob: Job? = null
) : PagedList.BoundaryCallback<T>(), ISupportDataSource {

    protected abstract val databaseHelper: RoomDatabase

    /**
     * Requires an instance of [kotlinx.coroutines.Job] or [kotlinx.coroutines.SupervisorJob]
     */
    override val supervisorJob = SupervisorJob(parentCoroutineJob)

    /**
     * Connectivity helper utility with live data observable capabilities
     */
    override val supportConnectivityHelper by inject<SupportConnectivityHelper>()

    protected val pagingRequestHelper = PagingRequestHelper(IO_EXECUTOR)

    override val networkState = pagingRequestHelper.createStatusLiveData()

    protected val supportPagingHelper: SupportPagingHelper?
        get() = bundle.getParcelable(SupportExtKeyStore.key_pagination)

    /**
     * Invokes dynamic requests which can be consumed which can be mapped
     * to a destination source on success
     */
    protected abstract fun startRequestForType(callback: PagingRequestHelper.Request.Callback)

    companion object {
        val IO_EXECUTOR: ExecutorService = Executors.newSingleThreadExecutor()
    }
}