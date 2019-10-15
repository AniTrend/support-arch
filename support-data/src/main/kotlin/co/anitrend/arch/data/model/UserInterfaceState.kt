package co.anitrend.arch.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import co.anitrend.arch.data.source.contract.IDataSource
import co.anitrend.arch.data.source.core.contract.ICoreDataSource
import co.anitrend.arch.data.source.paging.contract.IPagingDataSource
import co.anitrend.arch.domain.common.IUserInterfaceState
import co.anitrend.arch.domain.entities.NetworkState

/**
 * Model that view models create for UI components to observe on
 *
 * @param model LiveData for the UI to observe
 */
data class UserInterfaceState<T> internal constructor(
    val model: LiveData<T>,
    override val networkState: LiveData<NetworkState>,
    override val refreshState: LiveData<NetworkState>,
    override val refresh: () -> Unit,
    override val retry: () -> Unit
): IUserInterfaceState<LiveData<NetworkState>> {

    companion object {
        fun <T> create(
            model: LiveData<T>,
            source: IPagingDataSource
        ) : UserInterfaceState<T> {
            val refreshTrigger = MutableLiveData<Unit>()
            val refreshState = Transformations.switchMap(refreshTrigger) {
                MutableLiveData<NetworkState>()
            }

            return UserInterfaceState(
                model = model,
                networkState = source.networkState,
                refreshState = refreshState,
                refresh = {
                    source.invalidateAndRefresh()
                    refreshTrigger.value = null
                },
                retry = {
                    source.retryRequest()
                }
            )
        }

        fun <T> create(
            model: LiveData<T>,
            source: ICoreDataSource
        ) : UserInterfaceState<T> {
            val refreshTrigger = MutableLiveData<Unit>()
            val refreshState = Transformations.switchMap(refreshTrigger) {
                MutableLiveData<NetworkState>()
            }

            return UserInterfaceState(
                model = model,
                networkState = source.networkState,
                refreshState = refreshState,
                refresh = {
                    source.invalidateAndRefresh()
                    refreshTrigger.value = null
                },
                retry = {
                    source.retryRequest()
                }
            )
        }
    }
}