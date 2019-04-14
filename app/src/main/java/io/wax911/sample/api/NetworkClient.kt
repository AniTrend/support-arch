package io.wax911.sample.api

import io.wax911.support.core.controller.SupportRequestClient
import io.wax911.support.extension.logError
import io.wax911.support.core.wrapper.ModelWrapper
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import retrofit2.Call
import timber.log.Timber

class NetworkClient : SupportRequestClient() {

    /**
     * Executes the given retrofit call and returns a deferred result. This function call
     * will require that you call .await() to kick of the execution
     *
     * @param call retrofit call to execute
     */
    override fun <T> executeUsingAsync(call: Call<T>): Deferred<ModelWrapper<T?>> = async {
        try {
            callList.add(call)
            val response = call.execute()

            if (!response.isSuccessful)
                Timber.d(response.errorBody().logError())

            return@async ModelWrapper<T?>(response.code(),
                    response.body(),
                    response.headers(),
                    response.errorBody())
        } catch (e: Exception) {
            e.printStackTrace()
            return@async ModelWrapper<T?>()
        }
    }
}