package io.wax911.support.data.model.contract

import androidx.lifecycle.LiveData
import io.wax911.support.data.model.NetworkState

interface IUiModel {
    /**
    * Represents the network request status to show to the user
    */
    val networkState: LiveData<NetworkState>
    /**
     * Represents the refresh status to show to the user. Separate from networkState,
     * this value is importantly only when refresh is requested.
     */
    val refreshState: LiveData<NetworkState>
    /**
     * Refreshes the whole data and fetches it from scratch.
     */
    val refresh: () -> Unit
    /**
     * Retries any failed requests.
     */
    val retry: () -> Unit
}