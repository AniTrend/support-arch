package io.wax911.sample.data.api

import io.wax911.sample.data.extension.logError
import io.wax911.support.core.controller.SupportRequestClient
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import retrofit2.Call

@Deprecated("Use data source for handling responses and error validation")
class NetworkClient : SupportRequestClient() {

    /**
     * Executes the given retrofit call and returns a result. This function call
     * will require that you execute it in a async context to avoid exceptions
     * caused by running network calls on the main thread
     *
     * @param call retrofit call to execute
     */
    override fun <T> executeUsing(call: Call<T>): T? {
        return try {
            callList.add(call)
            val response = call.execute()

            if (!response.isSuccessful)
                response.errorBody().logError()
            return response.body()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Executes the given retrofit call and returns a deferred result. This function call
     * will require that you call .await() to kick of the execution
     *
     * @param call retrofit call to execute
     */
    override fun <T> executeUsingAsync(call: Call<T>): Deferred<T?> = async {
        return@async executeUsing(call)
    }
}