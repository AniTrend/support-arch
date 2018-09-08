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
import io.wax911.support.util.SupportUtil
import io.wax911.support.util.isConnectedToNetwork
import retrofit2.Call

abstract class CrudRepository<K, V> : RetroCallback<V> {

    protected val model : MutableLiveData<V> by lazy { MutableLiveData<V>() }
    protected var responseCallback: ResponseCallback<V>? = null
    private var requestClient: SupportRequestClient<V>? = null


    protected var requestType: Int = 0

    abstract fun save(model : V)

    abstract fun findOne(key : K)
    abstract fun findAll()

    abstract fun delete(model : V)

    abstract fun onCleared()

    fun registerObserver(context: LifecycleOwner, observer: Observer<V>) {
        when { !model.hasActiveObservers() -> model.observe(context, observer) }
    }

    /**
     * Creates the network client for implementing class
     *
     * @param parameters bundle of parameters for the request
     */
    abstract fun createNetworkClient(parameters: Bundle) : SupportRequestClient<V>

    /**
     * Handles dispatching of network requests to a background thread
     *
     * @param requestType type of request
     * @param parameters bundle of parameters for the request
     * @param context any valid context
     */
    fun requestFromNetwork(requestType: Int, parameters: Bundle, context: Context) {
        this.requestType = requestType
        when (context.isConnectedToNetwork()) {
            true -> {
                requestClient = createNetworkClient(parameters)
                requestClient?.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, context)
            }
            false -> requestFromCache(parameters)
        }
    }

    /**
     * When the application is not connected to the internet this method is called to resolve the
     * kind of content that needs to be fetched from the database
     *
     * @param parameters bundle of parameters for the request
     */
    protected abstract fun requestFromCache(parameters: Bundle)


    fun isProcessStatus(status : AsyncTask.Status) : Boolean =
            SupportUtil.equals(requestClient?.status, status)

    fun cancelPendingRequests(interruptExecution : Boolean) {
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
        responseCallback?.onResponseError(call, throwable)
    }
}
