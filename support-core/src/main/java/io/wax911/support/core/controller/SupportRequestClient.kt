package io.wax911.support.core.controller

import io.wax911.support.core.util.SupportCoroutineUtil
import io.wax911.support.core.wrapper.RetroWrapper
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
    abstract fun <T> executeUsingAsync(call: Call<T>): Deferred<RetroWrapper<T?>>

    /**
     * Executes the given retrofit call and returns a result. This function call
     * will require that you execute it in a async context to avoid exceptions
     * caused by running network calls on the main thread
     *
     * @param call retrofit call to execute
     */
    abstract fun <T> executeUsing(call: Call<T>): RetroWrapper<T?>

    /**
     * Cancels all the call requests that were used in the executeUsing function
     *
     * @see [io.wax911.support.core.controller.SupportRequestClient.executeUsingAsync]
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
