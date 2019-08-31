package co.anitrend.arch.data.source.contract

import androidx.lifecycle.MutableLiveData
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.util.SupportCoroutineHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 *
 *
 * @since v1.1.0
 */
interface IDataSource : SupportCoroutineHelper {

    /**
     * Observable for network state during requests that the UI can monitor and
     * act based on state changes
     */
    val networkState: MutableLiveData<NetworkState>

    /**
     * Coroutine dispatcher specification
     *
     * @return [kotlinx.coroutines.Dispatchers.Default] by default
     */
    override val coroutineDispatcher: CoroutineDispatcher
        get() =  Dispatchers.IO
}