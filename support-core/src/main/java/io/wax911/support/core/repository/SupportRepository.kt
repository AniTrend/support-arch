package io.wax911.support.core.repository

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.lifecycle.LiveData
import io.wax911.support.core.repository.contract.ISupportRepository
import io.wax911.support.core.view.model.NetworkState
import io.wax911.support.core.view.model.UiModel
import retrofit2.Response
import timber.log.Timber

abstract class SupportRepository<V, R>(context: Context): ISupportRepository<V> {

    override val connectivityManager: ConnectivityManager? = context.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager?


    /**
     * When the application is connected to the internet this method is called to resolve the
     * kind of content that needs to be fetched from the network using the given parameters
     *
     * @param bundle bundle of parameters for the request
     */
    protected abstract fun requestFromNetwork(bundle: Bundle): UiModel<V>

    /**
     * When the application is not connected to the internet this method is called to resolve the
     * kind of content that needs to be fetched from the database using the given parameters
     *
     * @param bundle bundle of parameters for the request
     */
    protected abstract fun requestFromCache(bundle: Bundle): UiModel<V>

    /**
     * When refresh is called, we simply run a fresh network request and when it arrives, clear
     * the database table and insert all new items in a transaction.
     *
     * Since the PagedList already uses a database bound data source, it will automatically be
     * updated after the database transaction is finished.
     */
    protected abstract fun refresh(bundle: Bundle): LiveData<NetworkState>


    /**
     * Inserts the response into the database while also assigning position indices to items.
     */
    protected abstract fun insertResultIntoDb(bundle: Bundle, value: Response<R?>)


    /**
     * Handles dispatching of network requests to a background thread, if the does not have
     * an active internet connection then data is requested from the database
     *
     * @param bundle bundle of parameters for the request
     */
    override fun invokeRequest(bundle: Bundle): UiModel<V> {
        return when (isConnectedToActiveNetwork()) {
            true -> requestFromNetwork(bundle)
            else -> requestFromCache(bundle)
        }
    }

    /**
     * Deals with cancellation of any pending or on going operations
     * that the repository is busy with
     *
     * @see [io.wax911.support.core.controller.contract.ISupportRequestClient.cancel]
     */
    override fun onCleared() {
        networkClient.cancel()
    }

    private fun isConnectedToActiveNetwork(): Boolean {
        return try {
            connectivityManager?.activeNetworkInfo?.isConnected ?: false
        } catch (e: Exception) {
            Timber.e(e, "Failed to check internet connectivity")
            false
        }
    }
}
