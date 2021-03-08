package co.anitrend.arch.data.state

import co.anitrend.arch.data.source.contract.IDataSource
import co.anitrend.arch.data.source.core.contract.AbstractDataSource
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.domain.state.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

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
data class DataState<T> internal constructor(
    val model: Flow<T>,
    override val networkState: Flow<NetworkState>,
    override val refreshState: Flow<NetworkState>,
    override val refresh: suspend () -> Unit,
    override val retry: suspend () -> Unit
): UiState<Flow<NetworkState>>() {

    companion object {

        /**
         * Helper for creating a user interface state using a data source
         *
         * @param model The requested result as an observable
         *
         * @see AbstractDataSource
         */
        infix fun <T> IDataSource.create(
            model: Flow<T>
        ) : DataState<T> {
            val refreshTrigger: MutableStateFlow<NetworkState> =
                MutableStateFlow(NetworkState.Idle)

            return DataState(
                model = model,
                networkState = networkState,
                refreshState = refreshTrigger,
                refresh = {
                    refreshTrigger.value = NetworkState.Loading
                    refresh()
                    refreshTrigger.value = NetworkState.Success
                },
                retry = {
                    retryFailed()
                }
            )
        }
    }
}