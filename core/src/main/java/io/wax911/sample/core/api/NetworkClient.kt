package io.wax911.sample.core.api

import io.wax911.sample.core.extension.logError
import io.wax911.support.core.controller.SupportRequestClient
import io.wax911.support.core.wrapper.RequestResult
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import retrofit2.Call

class NetworkClient : SupportRequestClient() {
    /**
     * Executes the given retrofit call and returns a result. This function call
     * will require that you execute it in a async context to avoid exceptions
     * caused by running network calls on the main thread
     *
     * @param call retrofit call to execute
     */
    override fun <T> executeUsing(call: Call<T>): RequestResult<T?> {
        try {
            callList.add(call)
            val response = call.execute()

            if (!response.isSuccessful)
                response.errorBody().logError()

            return RequestResult(response.code(),
                response.body(),
                response.headers(),
                response.errorBody())
        } catch (e: Exception) {
            e.printStackTrace()
            return RequestResult()
        }
    }

    /**
     * Executes the given retrofit call and returns a deferred result. This function call
     * will require that you call .await() to kick of the execution
     *
     * @param call retrofit call to execute
     */
    override fun <T> executeUsingAsync(call: Call<T>): Deferred<RequestResult<T?>> = async {
        return@async executeUsing(call)
    }
}