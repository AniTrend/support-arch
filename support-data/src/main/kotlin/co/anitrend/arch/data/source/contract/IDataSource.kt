package co.anitrend.arch.data.source.contract

import androidx.lifecycle.MutableLiveData
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.coroutine.SupportCoroutine

/**
 * Contract for data sources
 *
 * @since v1.1.0
 */
interface IDataSource : SupportCoroutine {

    /**
     * Observable for network state during requests that the UI can monitor and
     * act based on state changes
     */
    val networkState: MutableLiveData<NetworkState>
}