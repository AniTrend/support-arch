package io.wax911.support.core.repository

import android.os.Bundle
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import androidx.paging.extension.createStatusLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor
import java.util.concurrent.Executors

abstract class SupportBoundaryCallback<T, R>(
    protected val bundle: Bundle,
    protected val responseHandler: (Bundle, Response<R?>) -> Unit
) : PagedList.BoundaryCallback<T>() {

    val pagingRequestHelper = PagingRequestHelper(
        IO_EXECUTOR
    )

    val networkState = pagingRequestHelper.createStatusLiveData()

    companion object {
        val IO_EXECUTOR = Executors.newSingleThreadExecutor()
        val NETWORK_EXECUTOR = Executors.newFixedThreadPool(4)
    }

    /**
     * Every time it gets new items, boundary callback simply inserts them into the database and
     * paging library takes care of refreshing the list if necessary.
     */
    protected abstract fun insertItemsIntoDb(response: Response<R?>, requestCallback: PagingRequestHelper.Request.Callback)

    protected fun createWebserviceCallback(requestCallback: PagingRequestHelper.Request.Callback): Callback<R> {
        return object : Callback<R> {
            override fun onFailure(call: Call<R>, throwable: Throwable) {
                requestCallback.recordFailure(throwable)
            }

            override fun onResponse(call: Call<R>, response: Response<R?>) {
                // insertItemsIntoDb(response, requestCallback)
            }
        }
    }
}