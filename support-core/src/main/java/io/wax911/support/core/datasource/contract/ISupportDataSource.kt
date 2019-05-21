package io.wax911.support.core.datasource.contract

import androidx.lifecycle.MutableLiveData
import io.wax911.support.core.util.SupportCoroutineUtil
import io.wax911.support.core.view.model.NetworkState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface ISupportDataSource : SupportCoroutineUtil {

    val networkState: MutableLiveData<NetworkState>
    val initialLoad: MutableLiveData<NetworkState>

    fun retryAllFailedRequests()
    fun refreshOrInvalidate()

    /**
     * Coroutine dispatcher specification
     *
     * @return [kotlinx.coroutines.Dispatchers.Default] by default
     */
    override val coroutineDispatcher: CoroutineDispatcher
        get() =  Dispatchers.IO
}