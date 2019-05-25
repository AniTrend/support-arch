package io.wax911.support.core.repository.contract

import android.os.Bundle
import io.wax911.support.core.util.SupportConnectivityHelper
import io.wax911.support.core.util.SupportCoroutineHelper
import io.wax911.support.core.view.model.UiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinComponent

/**
 * Repository contract
 */
interface ISupportRepository<V> : SupportCoroutineHelper, KoinComponent {

    val supportConnectivityHelper: SupportConnectivityHelper

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