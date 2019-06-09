package io.wax911.support.data.model

import androidx.lifecycle.LiveData
import io.wax911.support.data.model.contract.IUiModel

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
data class UiModel<T>(
    val model: LiveData<T>,
    override val networkState: LiveData<NetworkState>,
    override val refreshState: LiveData<NetworkState>,
    override val refresh: () -> Unit,
    override val retry: () -> Unit
): IUiModel