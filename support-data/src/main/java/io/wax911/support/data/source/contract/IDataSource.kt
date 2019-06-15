package io.wax911.support.data.source.contract

import androidx.lifecycle.LiveData
import io.wax911.support.data.model.NetworkState
import io.wax911.support.extension.util.SupportCoroutineHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface IDataSource : SupportCoroutineHelper {

    /**
     * Observable for network state during requests that the UI can monitor and
     * act based on state changes
     */
    val networkState: LiveData<NetworkState>

    /**
     * Coroutine dispatcher specification
     *
     * @return [kotlinx.coroutines.Dispatchers.Default] by default
     */
    override val coroutineDispatcher: CoroutineDispatcher
        get() =  Dispatchers.IO
}