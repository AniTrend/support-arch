package io.wax911.support.base.dao

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.wax911.support.base.event.ResponseCallback
import io.wax911.support.base.event.RetroCallback
import io.wax911.support.custom.worker.SupportRequestClient
import io.wax911.support.equal
import io.wax911.support.isConnectedToNetwork
import retrofit2.Call

abstract class CrudRepository<K, V>(private val responseCallback: ResponseCallback<V>) : RetroCallback<V> {

    lateinit var modelDao: QueryBase<V>

    protected lateinit var parameters: Bundle

    protected val mutableLiveData : MutableLiveData<V> by lazy { MutableLiveData<V>() }

    private var requestClient: SupportRequestClient<V>? = null

    protected var requestType: Int = 0

    abstract fun save(model : V)

    abstract fun find(key : K)
    open fun find() {
        // optional implementation
    }

    abstract fun delete(model : V)

    fun registerObserver(context: LifecycleOwner, observer: Observer<V>) = when {
        !mutableLiveData.hasActiveObservers() -> mutableLiveData.observe(context, observer)
        else -> { }
    }


    /**
     * Creates the network client for implementing class
     *
     * @param parameters bundle of parameters for the request
     */
    abstract fun createNetworkClient() : SupportRequestClient<V>

    /**
     * Handles dispatching of network requests to a background thread
     *
     * @param requestType type of request
     * @param parameters bundle of parameters for the request
     * @param context any valid context
     */
    fun requestFromNetwork(requestType: Int, parameters: Bundle, context: Context) {
        this.requestType = requestType
        this.parameters = parameters
        when (context.isConnectedToNetwork()) {
            true -> {
                requestClient = createNetworkClient()
                requestClient?.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, context)
            }
            false -> requestFromCache()
        }
    }

    /**
     * When the application is not connected to the internet this method is called to resolve the
     * kind of content that needs to be fetched from the database
     */
    protected abstract fun requestFromCache()


    private fun isProcessStatus(status : AsyncTask.Status) : Boolean =
            requestClient?.status.equal(status)

    private fun cancelPendingRequests(interruptExecution : Boolean) {
        if(!isProcessStatus(AsyncTask.Status.FINISHED))
            requestClient?.cancel(interruptExecution)
    }

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     *
     * @param call the origination requesting object
     * @param throwable contains information about the error
     */
    override fun onFailure(call: Call<V>, throwable: Throwable) {
        throwable.printStackTrace()
        responseCallback.onResponseError(call, throwable)
    }

    open fun onCleared() {
        if (!isProcessStatus(AsyncTask.Status.FINISHED))
            cancelPendingRequests(true)
    }
}
