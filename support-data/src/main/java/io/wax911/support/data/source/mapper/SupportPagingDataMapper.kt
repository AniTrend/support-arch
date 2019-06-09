package io.wax911.support.data.source.mapper

import androidx.paging.PagingRequestHelper
import io.wax911.support.data.source.mapper.contract.ISupportDataMapper
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

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

    override val responseCallback = object : Callback<S> {

        /**
         * Invoked when a network exception occurred talking to the server or when an unexpected
         * exception occurred creating the request or processing the response.
         */
        override fun onFailure(call: Call<S>, throwable: Throwable) {
            pagingRequestCallback.recordFailure(throwable)
        }

        /**
         * Invoked for a received HTTP response.
         *
         * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
         * Call [Response.isSuccessful] to determine if the response indicates success.
         */
        override fun onResponse(call: Call<S>, response: Response<S?>) {
            when (response.isSuccessful) {
                true -> {
                    launch {
                        val mapped = onResponseMapFrom(response)
                        onResponseDatabaseInsert(mapped)
                        pagingRequestCallback.recordSuccess()
                    }
                }
                false -> {
                    val message = response.message()
                    val throwable = Throwable(message)
                    Timber.tag(moduleTag).e(throwable)
                    pagingRequestCallback.recordFailure(throwable)
                }
            }
        }
    }
}