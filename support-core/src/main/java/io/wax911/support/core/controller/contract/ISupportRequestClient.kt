package io.wax911.support.core.controller.contract

import io.wax911.support.core.util.SupportCoroutineHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import retrofit2.Call

interface ISupportRequestClient : SupportCoroutineHelper {

    /**
     * Executes the given retrofit call and returns a deferred result. This function call
     * will require that you call .await() to kick of the execution
     *
     * @param call retrofit call to execute
     */
    fun <T> executeUsingAsync(call: Call<T>): Deferred<T?>

    /**
     * Executes the given retrofit call and returns a result. This function call
     * will require that you execute it in a async context to avoid exceptions
     * caused by running network calls on the main thread
     *
     * @param call retrofit call to execute
     */
    fun <T> executeUsing(call: Call<T>): T?

    /**
     * Cancels all the call requests that were used in the executeUsing function
     *
     * @see [io.wax911.support.core.controller.SupportRequestClient.executeUsingAsync]
     */
    fun cancel()

    /**
     * Coroutine dispatcher specification
     *
     * @return [kotlinx.coroutines.Dispatchers.Default] by default
     */
    override val coroutineDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO
}