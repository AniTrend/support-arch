package io.wax911.support.data.source.contract

import android.os.Bundle
import androidx.lifecycle.LiveData
import io.wax911.support.data.model.NetworkState
import io.wax911.support.extension.util.SupportConnectivityHelper
import io.wax911.support.extension.util.SupportCoroutineHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinComponent

interface ISupportDataSource : KoinComponent, SupportCoroutineHelper {

    /**
     * Connectivity helper utility with live data observable capabilities
     */
    val supportConnectivityHelper: SupportConnectivityHelper

    /**
     * Observable for network state during requests that the UI can monitor and
     * act based on state changes
     */
    val networkState: LiveData<NetworkState>

    /**
     * Clears all the data in a database table which will assure that
     * and refresh the backing storage medium with new network data
     */
    fun refreshOrInvalidate()

    /**
     * Performs the necessary operation to invoke a network retry request
     */
    fun retryFailedRequest()

    /**
     * Coroutine dispatcher specification
     *
     * @return [kotlinx.coroutines.Dispatchers.Default] by default
     */
    override val coroutineDispatcher: CoroutineDispatcher
        get() =  Dispatchers.IO

    interface IDataSourceObservable<O> {

        /**
         * Returns the appropriate observable which we will monitor for updates,
         * common implementation may include but not limited to returning
         * data source live data for a database
         *
         * @param bundle request params, implementation is up to the developer
         */
        fun observerOnLiveDataWith(bundle: Bundle): LiveData<O>
    }
}