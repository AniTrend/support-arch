package io.wax911.support.data.repository.contract

import android.os.Bundle
import io.wax911.support.data.model.UiModel
import io.wax911.support.extension.util.SupportCoroutineHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinComponent


interface ISupportRepository<V> : SupportCoroutineHelper, KoinComponent {

    /**
     * Handles dispatching of network requests to a background thread
     *
     * @param bundle bundle of parameters for the request
     */
    fun invokeRequest(bundle: Bundle): UiModel<V>

    /**
     * Deals with cancellation of any pending or on going operations that the repository is busy with
     */
    fun onCleared() {
        cancelAllChildren()
    }

    override val coroutineDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO
}