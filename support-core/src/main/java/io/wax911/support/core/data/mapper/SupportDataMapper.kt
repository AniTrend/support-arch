package io.wax911.support.core.data.mapper

import androidx.paging.PagingRequestHelper
import io.wax911.support.core.data.mapper.contract.ISupportDataMapper
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

abstract class SupportDataMapper<S, D>(
    private val pagingRequestHelper: PagingRequestHelper.Request.Callback
) : ISupportDataMapper<S, D> {

    override val responseCallback = object : Callback<S> {

        /**
         * Invoked when a network exception occurred talking to the server or when an unexpected
         * exception occurred creating the request or processing the response.
         */
        override fun onFailure(call: Call<S>, throwable: Throwable) {
            pagingRequestHelper.recordFailure(throwable)
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
                        pagingRequestHelper.recordSuccess()
                    }
                }
                false -> {
                    val message = response.message()
                    val throwable = Throwable(message)
                    Timber.tag(TAG).e(throwable)
                    pagingRequestHelper.recordFailure(throwable)
                }
            }
        }
    }
}