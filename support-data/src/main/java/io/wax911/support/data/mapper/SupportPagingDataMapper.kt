package io.wax911.support.data.mapper

import androidx.paging.PagingRequestHelper
import io.wax911.support.data.model.NetworkState
import io.wax911.support.data.mapper.contract.ISupportDataMapper
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import retrofit2.Response

/**
 * Provides functionality for mapping objects from one type to another
 *
 * @param parentCoroutineJob parent coroutine from something that is lifecycle aware,
 * this enables us to cancels jobs automatically when the parent is also canceled
 */
abstract class SupportPagingDataMapper<S, D>(
    private val pagingRequestCallback: PagingRequestHelper.Request.Callback,
    parentCoroutineJob: Job? = null
) : ISupportDataMapper<S, D> {

    protected val moduleTag: String = javaClass.simpleName

    /**
     * Requires an instance of [kotlinx.coroutines.Job] or [kotlinx.coroutines.SupervisorJob]
     */
    override val supervisorJob = SupervisorJob(parentCoroutineJob)
}