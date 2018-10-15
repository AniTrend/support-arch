package io.wax911.support.base.dao

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.wax911.support.isConnectedToNetwork
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

abstract class SupportRepository<K, V> {

    private var repositoryJob : Job? = null

    protected var modelDao: QueryBase<V>? = null

    protected val mutableLiveData : MutableLiveData<V?>
            by lazy { MutableLiveData<V?>() }

    open fun save(model : V): Deferred<Unit?> = async { modelDao?.insert(model) }

    open fun find(key : K): Deferred<V?>  = async { null }

    open fun find(): Deferred<V?> = async { null }

    open fun delete(model : V): Deferred<Unit?> = async { modelDao?.delete(model) }

    fun registerObserver(context: LifecycleOwner, observer: Observer<V?>) = when {
        !mutableLiveData.hasActiveObservers() -> mutableLiveData.observe(context, observer)
        else -> { }
    }

    /**
     * Creates the network client for implementing class using the given parameters
     *
     * @param bundle bundle of parameters for the request
     */
    protected abstract fun createNetworkClientRequest(bundle: Bundle, context: Context): Deferred<Unit>

    /**
     * When the application is not connected to the internet this method is called to resolve the
     * kind of content that needs to be fetched from the database using the given parameters
     *
     * @param bundle bundle of parameters for the request
     */
    protected abstract fun requestFromCache(bundle: Bundle, context: Context): Deferred<Unit>

    /**
     * Handles dispatching of network requests to a background thread
     *
     * @param bundle bundle of parameters for the request
     * @param context any valid context
     */
    fun requestFromNetwork(bundle: Bundle, context: Context?) {
        context?.also {
            repositoryJob = when (it.isConnectedToNetwork()) {
                true -> launch { createNetworkClientRequest(bundle, it).await() }
                false -> launch { requestFromCache(bundle, it).await() }
            }
        }
    }

    open fun onCleared() = repositoryJob?.cancel()
}
