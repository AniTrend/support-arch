package io.wax911.support.core.view.model

import androidx.lifecycle.LiveData
import io.wax911.support.core.view.model.contract.IUiModel

data class UiModel<T>(
    /**
     *  LiveData for the UI to observe
     */
    val model: LiveData<T>,
    /**
     * Represents the network request status to show to the user
     */
    override val networkState: LiveData<NetworkState>,
    /**
     * Represents the refresh status to show to the user. Separate from networkState,
     * this value is importantly only when refresh is requested.
     */
    override val refreshState: LiveData<NetworkState>,
    /**
     * Refreshes the whole data and fetches it from scratch.
     */
    override val refresh: () -> Unit,
    /**
     * Retries any failed requests.
     */
    override val retry: () -> Unit
): IUiModel