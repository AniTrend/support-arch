package co.anitrend.arch.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import co.anitrend.arch.data.source.core.contract.ICoreDataSource
import co.anitrend.arch.data.source.coroutine.contract.ICoroutineDataSource
import co.anitrend.arch.data.source.paging.contract.IPagingDataSource
import co.anitrend.arch.domain.common.IUserInterfaceState
import co.anitrend.arch.domain.entities.NetworkState
import kotlinx.coroutines.launch

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
        fun <T> ICoroutineDataSource<T>.create(
            model: LiveData<T>
        ) : UserInterfaceState<T> {
            val refreshTrigger = MutableLiveData<NetworkState>()
            val refreshState = Transformations.switchMap(refreshTrigger) {
                val state = MutableLiveData<NetworkState>()
                state.postValue(it)
                state
            }

            return UserInterfaceState(
                model = model,
                networkState = networkState,
                refreshState = refreshState,
                refresh = {
                    launch {
                        invalidateAndRefresh()
                        refreshTrigger.postValue(
                            NetworkState.Loading
                        )
                    }
                },
                retry = {
                    launch {
                        retryRequest()
                    }
                }
            )
        }

        fun <T> IPagingDataSource.create(
            model: LiveData<T>
        ) : UserInterfaceState<T> {
            val refreshTrigger = MutableLiveData<NetworkState>()
            val refreshState = Transformations.switchMap(refreshTrigger) {
                val state = MutableLiveData<NetworkState>()
                state.postValue(it)
                state
            }

            return UserInterfaceState(
                model = model,
                networkState = networkState,
                refreshState = refreshState,
                refresh = {
                    invalidateAndRefresh()
                    refreshTrigger.value = NetworkState.Loading
                },
                retry = {
                    retryRequest()
                }
            )
        }

        fun <T> ICoreDataSource.create(
            model: LiveData<T>
        ) : UserInterfaceState<T> {
            val refreshTrigger = MutableLiveData<NetworkState>()
            val refreshState = Transformations.switchMap(refreshTrigger) {
                val state = MutableLiveData<NetworkState>()
                state.postValue(it)
                state
            }

            return UserInterfaceState(
                model = model,
                networkState = networkState,
                refreshState = refreshState,
                refresh = {
                    invalidateAndRefresh()
                    refreshTrigger.value = NetworkState.Loading
                },
                retry = {
                    retryRequest()
                }
            )
        }
    }
}