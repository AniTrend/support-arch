package io.wax911.support.custom.controller

import io.wax911.support.model.ModelWrapper
import kotlinx.coroutines.Deferred
import retrofit2.Call

abstract class SupportRequestClient {

    protected val callList: MutableList<Call<*>> by lazy { ArrayList<Call<*>>() }

    /**
     * Executes the given retrofit call and returns a deferred result. This function call
     * will require that you call .await() to kick of the execution
     * <br/>
     *
     * @param call retrofit call to execute
     */
    abstract fun <T> executeUsing(call: Call<T>): Deferred<ModelWrapper<T?>>

    /**
     * Cancels all the call requests that were used in the executeUsing function
     * <br/>
     *
     * @see executeUsing
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
