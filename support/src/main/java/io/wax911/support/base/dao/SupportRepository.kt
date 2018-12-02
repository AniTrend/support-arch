package io.wax911.support.base.dao

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.wax911.support.custom.worker.SupportRequestClient
import io.wax911.support.isConnectedToNetwork
import kotlinx.coroutines.*

abstract class SupportRepository<K, V> {

    private var repositoryJob : Job? = null
    private var disposableHandle : DisposableHandle? = null

    protected val networkClient: SupportRequestClient by lazy { initNetworkClient() }
    protected var modelDao: QueryBase<V>? = null

    val liveData : MutableLiveData<V?> by lazy {
        MutableLiveData<V?>()
    }

    /**
     * Saves the given model to the database
     * <br/>
     *
     * @param model item which should be saved
     */
    open fun save(model : V) = modelDao?.insert(model)

    /**
     * Find any specific items from our database using a key
     */
    open fun find(key : K) : V? = null

    /**
     * Find any specific items from our database
     */
    open fun find() : V? = null

    /**
     * Deletes the given model from the database
     * <br/>
     *
     * @param model item which should be deleted
     */
    open fun delete(model : V) { modelDao?.delete(model) }

    /**
     * Sets the given life cycle owner to observe changes in the live data that
     * currently exists in this repository.
     * <br/>
     *
     * @param context any valid life cycle owner such as a FragmentActivity descendant
     * @param observer any observer that shares the same value type as this repository
     */
    fun registerObserver(context: LifecycleOwner, observer: Observer<V?>) {
        if (!liveData.hasActiveObservers())
            liveData.observe(context, observer)
    }

    /**
     * Requires the network client to be created in the implementing repo,
     * to access the created client please use:
     * <br/>
     *
     * @see networkClient
     */
    protected abstract fun initNetworkClient(): SupportRequestClient

    /**
     * Creates the network client for implementing class using the given parameters
     * <br/>
     *
     * @param bundle bundle of parameters for the request
     */
    protected abstract fun createNetworkClientRequest(bundle: Bundle, context: Context): Deferred<Unit>

    /**
     * When the application is not connected to the internet this method is called to resolve the
     * kind of content that needs to be fetched from the database using the given parameters
     * <br/>
     *
     * @param bundle bundle of parameters for the request
     */
    protected abstract fun requestFromCache(bundle: Bundle, context: Context): Deferred<Unit>

    /**
     * Dispatches the results to be set to the live data to a UI thread
     * <br/>
     *
     * @param results data that needs to be sent to the view responsible for creating the request
     */
    protected suspend fun publishResult(results : V?) = withContext(Dispatchers.Main) {
        liveData.value = results
    }


    /**
     * Handles dispatching of network requests to a background thread
     * <br/>
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

    /**
     * Deals with cancellation of any pending or on going operations that the repository is busy with
     */
    open fun onCleared() {
        networkClient.cancel()
        repositoryJob?.cancel()
        disposableHandle?.dispose()
    }
}
