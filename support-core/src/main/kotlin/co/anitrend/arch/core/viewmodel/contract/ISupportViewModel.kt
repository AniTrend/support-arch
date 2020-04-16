package co.anitrend.arch.core.viewmodel.contract

import androidx.lifecycle.LiveData
import co.anitrend.arch.domain.entities.NetworkState

/**
 * Contract for view models that aids relaying commands to an underlying repository
 *
 * @since v0.9.X
 */
@Deprecated(
    message = "Enforces implementation to follow an implementation style which might not be desirable",
    replaceWith = ReplaceWith(
        "ISupportViewState",
        "co.anitrend.arch.core.model.ISupportViewState"
    )
)
interface ISupportViewModel<P, R> {

    /**
     * Use case result model
     */
    val model: LiveData<R?>

    /**
     * Network state for main requests
     */
    val networkState: LiveData<NetworkState>?

    /**
     * Refreshing network state
     */
    val refreshState: LiveData<NetworkState>?

    /**
     * Checks if the live data stored in the repository has is not null
     *
     * @return [Boolean] true or false
     */
    fun hasModelData(): Boolean = model.value != null

    /**
     * Starts view model operations
     *
     * @param parameter request payload
     */
    operator fun invoke(parameter: P)

    /**
     * Requests the use case to perform a retry operation
     */
    fun retry()

    /**
     * Requests the use case to perform a refresh operation
     */
    fun refresh()
}