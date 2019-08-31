package co.anitrend.arch.data.model

import androidx.lifecycle.LiveData
import co.anitrend.arch.domain.common.IUserInterfaceState
import co.anitrend.arch.domain.entities.NetworkState

/**
 * Model that view models create for UI components to observe on
 *
 * @param model LiveData for the UI to observe
 */
data class UserInterfaceState<T>(
    val model: LiveData<T>,
    override val networkState: LiveData<NetworkState>,
    override val refreshState: LiveData<NetworkState>,
    override val refresh: () -> Unit,
    override val retry: () -> Unit
): IUserInterfaceState<LiveData<NetworkState>>