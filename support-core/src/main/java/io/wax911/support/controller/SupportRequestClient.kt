package io.wax911.support.controller

import io.wax911.support.util.SupportCoroutineUtil
import io.wax911.support.wrapper.ModelWrapper
import kotlinx.coroutines.Deferred
import retrofit2.Call

abstract class SupportRequestClient: SupportCoroutineUtil {

    protected val callList: MutableList<Call<*>> by lazy { ArrayList<Call<*>>() }

    /**
     * Executes the given retrofit call and returns a deferred result. This function call
     * will require that you call .await() to kick of the execution
     *
     * @param call retrofit call to execute
     */
    abstract fun <T> executeUsingAsync(call: Call<T>): Deferred<ModelWrapper<T?>>

    /**
     * Cancels all the call requests that were used in the executeUsing function
     *
     * @see [io.wax911.support.controller.SupportRequestClient.executeUsingAsync]
     */
    fun cancel() {
        try {
            callList.forEach { it.cancel() }
            callList.clear()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
