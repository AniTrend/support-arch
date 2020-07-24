package co.anitrend.arch.data.model

import androidx.lifecycle.LiveData
import co.anitrend.arch.data.source.core.contract.ICoreDataSource
import co.anitrend.arch.data.source.coroutine.contract.ICoroutineDataSource
import co.anitrend.arch.data.source.paging.contract.IPagingDataSource
import co.anitrend.arch.domain.common.IUserInterfaceState
import co.anitrend.arch.domain.entities.NetworkState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

/**
 * Model that view models create for UI components to observe on
 *
 * @param model LiveData for the UI to observe
 * @param networkState Network request status to show to the user
 * @param refreshState Refresh status to show to the user. Separate from [networkState],
 * this value is importantly only when refresh is requested
 * @param refresh Refreshes & invalidates underlying data source fetches it from scratch.
 * @param retry Retries any failed requests.
 */
data class UserInterfaceState<T> internal constructor(
    val model: LiveData<T>,
    override val networkState: StateFlow<NetworkState>,
    override val refreshState: Flow<NetworkState>,
    override val refresh: () -> Unit,
    override val retry: () -> Unit
): IUserInterfaceState<Flow<NetworkState>> {

    companion object {

        /**
         * Helper for creating a user interface state using a coroutine driven data source
         *
         * @param model The requested result as an observable
         *
         * @see ICoroutineDataSource
         */
        fun <T> ICoroutineDataSource.create(
            model: LiveData<T>
        ) : UserInterfaceState<T> {
            val refreshTrigger = MutableStateFlow<NetworkState>(NetworkState.Idle)
            val refreshState: Flow<NetworkState> = refreshTrigger.flatMapLatest {
                val state = MutableStateFlow<NetworkState>(NetworkState.Loading)
                state.value = it
                state
            }

            return UserInterfaceState(
                model = model,
                networkState = networkState,
                refreshState = refreshState,
                refresh = {
                    launch {
                        invalidateAndRefresh()
                        refreshTrigger.value = NetworkState.Success
                    }
                },
                retry = {
                    launch {
                        retryRequest()
                    }
                }
            )
        }

        /**
         * Helper for creating a user interface state using a paging driven data source
         *
         * @param model The requested result as an observable
         *
         * @see IPagingDataSource
         */
        fun <T> IPagingDataSource.create(
            model: LiveData<T>
        ) : UserInterfaceState<T> {
            val refreshTrigger = MutableStateFlow<NetworkState>(NetworkState.Idle)
            val refreshState: Flow<NetworkState> = refreshTrigger.flatMapLatest {
                val state = MutableStateFlow<NetworkState>(NetworkState.Loading)
                state.value = it
                state
            }

            return UserInterfaceState(
                model = model,
                networkState = networkState,
                refreshState = refreshState,
                refresh = {
                    invalidateAndRefresh()
                    refreshTrigger.value = NetworkState.Success
                },
                retry = {
                    retryRequest()
                }
            )
        }

        /**
         * Helper for creating a user interface state using a data source
         *
         * @param model The requested result as an observable
         *
         * @see ICoreDataSource
         */
        fun <T> ICoreDataSource.create(
            model: LiveData<T>
        ) : UserInterfaceState<T> {
            val refreshTrigger = MutableStateFlow<NetworkState>(NetworkState.Idle)
            val refreshState: Flow<NetworkState> = refreshTrigger.flatMapLatest {
                val state = MutableStateFlow<NetworkState>(NetworkState.Loading)
                state.value = it
                state
            }

            return UserInterfaceState(
                model = model,
                networkState = networkState,
                refreshState = refreshState,
                refresh = {
                    invalidateAndRefresh()
                    refreshTrigger.value = NetworkState.Success
                },
                retry = {
                    retryRequest()
                }
            )
        }
    }
}