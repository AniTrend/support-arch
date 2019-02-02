package io.wax911.support.repository

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.wax911.support.controller.SupportRequestClient
import io.wax911.support.dao.SupportQuery
import io.wax911.support.isConnectedToNetwork
import io.wax911.support.repository.contract.ISupportRepository
import io.wax911.support.util.SupportCoroutineUtil
import kotlinx.coroutines.*
import java.lang.Exception

abstract class SupportRepository<K, V>: ISupportRepository<K, V>, SupportCoroutineUtil {

    protected var modelDao: SupportQuery<V>? = null
    protected val networkClient: SupportRequestClient by lazy { initNetworkClient() }

    val liveData : MutableLiveData<V?> by lazy { MutableLiveData<V?>() }

    /**
     * Saves the given model to the database
     * <br/>
     *
     * @param model item which should be saved
     */
    override fun save(model : V) { modelDao?.insert(model) }

    /**
     * Updates the given model to the database
     * <br/>
     *
     * @param model item which should be updated
     */
    override fun update(model : V) { modelDao?.update(model) }

    /**
     * Deletes the given model from the database
     * <br/>
     *
     * @param model item which should be deleted
     */
    override fun delete(model : V) { modelDao?.delete(model) }

    /**
     * Sets the given life cycle owner to observe changes in the live data that
     * currently exists in this repository.
     * <br/>
     *
     * @param context any valid life cycle owner such as a FragmentActivity descendant
     * @param observer any observer that shares the same value type as this repository
     */
    override fun registerObserver(context: LifecycleOwner, observer: Observer<V?>) {
        if (!liveData.hasActiveObservers())
            liveData.observe(context, observer)
    }

    /**
     * Requires the network client to be created in the implementing repo,
     * to access the created client.
     *
     * @see networkClient
     */
    protected abstract fun initNetworkClient(): SupportRequestClient

    /**
     * Creates the network client for implementing class using the given parameters
     *
     * @param bundle bundle of parameters for the request
     */
    protected abstract fun createNetworkClientRequestAsync(bundle: Bundle, context: Context): Deferred<Unit>

    /**
     * When the application is not connected to the internet this method is called to resolve the
     * kind of content that needs to be fetched from the database using the given parameters
     *
     * @param bundle bundle of parameters for the request
     */
    protected abstract fun requestFromCacheAsync(bundle: Bundle, context: Context): Deferred<Unit>

    /**
     * Dispatches the results to be set to the live data to a UI thread
     *
     * @param results data that needs to be sent to the view responsible for creating the request
     */
    protected suspend fun publishResult(results : V?) = withContext(Dispatchers.Main) {
        try {
            liveData.value = results
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * Handles dispatching of network requests to a background thread, if the does not have
     * an active internet connection then data is requested from the database
     *
     * @param bundle bundle of parameters for the request
     * @param context any valid context
     */
    override fun requestFromNetwork(bundle: Bundle, context: Context?) {
        context?.apply {
            async {
                when (isConnectedToNetwork()) {
                    true -> createNetworkClientRequestAsync(bundle, this@apply).await()
                    false -> requestFromCacheAsync(bundle, this@apply).await()
                }
            }.invokeOnCompletion { cause : Throwable? ->
                cause?.apply {
                    printStackTrace()
                    launch {
                        publishResult(null)
                    }
                }
            }
        }
    }

    /**
     * Deals with cancellation of any pending or on going operations
     * that the repository is busy with
     *
     * @see [io.wax911.support.controller.SupportRequestClient.cancel]
     */
    override fun onCleared() {
        networkClient.cancel()
        cancelAllChildren()
    }
}
