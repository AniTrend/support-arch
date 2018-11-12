package io.wax911.support.base.dao

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.wax911.support.isConnectedToNetwork
import kotlinx.coroutines.*

abstract class SupportRepository<K, V> {

    private var repositoryJob : Job? = null
    private var disposableHandle : DisposableHandle? = null

    protected var modelDao: QueryBase<V>? = null

    val liveData : MutableLiveData<V?> by lazy {
        MutableLiveData<V?>()
    }

    open fun save(model : V) = modelDao?.insert(model)

    open fun delete(model : V) { modelDao?.delete(model) }

    open fun find(key : K) : V? = null

    open fun find() : V? = null

    fun registerObserver(context: LifecycleOwner, observer: Observer<V?>) = when {
        !liveData.hasActiveObservers() -> liveData.observe(context, observer)
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
     * Dispatches the results to be set to the live data to a UI thread
     *
     * @param results data that needs to be sent to the view responsible for creating the request
     */
    protected suspend fun publishResult(results : V?) = withContext(Dispatchers.Main) {
        liveData.value = results
    }


    /**
     * Handles dispatching of network requests to a background thread
     *
     * @param bundle bundle of parameters for the request
     * @param context any valid context
     */
    fun requestFromNetwork(bundle: Bundle, context: Context?) {
        context?.also { it ->
            repositoryJob = GlobalScope.async {
                when (it.isConnectedToNetwork()) {
                    true -> createNetworkClientRequest(bundle, it).await()
                    false -> requestFromCache(bundle, it).await()
                }
            }
            disposableHandle = repositoryJob?.invokeOnCompletion { j ->
                j?.also { throwable ->
                    throwable.printStackTrace()
                    try {
                        GlobalScope.launch { publishResult(null) }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    open fun onCleared() {
        repositoryJob?.cancel()
        disposableHandle?.dispose()
    }
}
