package io.wax911.sample.api

import io.wax911.sample.extension.logError
import io.wax911.support.core.controller.SupportRequestClient
import io.wax911.support.core.wrapper.RetroWrapper
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import retrofit2.Call

class NetworkClient : SupportRequestClient() {

    /**
     * Executes the given retrofit call and returns a deferred result. This function call
     * will require that you call .await() to kick of the execution
     *
     * @param call retrofit call to execute
     */
    override fun <T> executeUsingAsync(call: Call<T>): Deferred<RetroWrapper<T?>> = async {
        try {
            callList.add(call)
            val response = call.execute()

            if (!response.isSuccessful)
                response.errorBody().logError()

            return@async RetroWrapper<T?>(response.code(),
                    response.body(),
                    response.headers(),
                    response.errorBody())
        } catch (e: Exception) {
            e.printStackTrace()
            return@async RetroWrapper<T?>()
        }
    }
}