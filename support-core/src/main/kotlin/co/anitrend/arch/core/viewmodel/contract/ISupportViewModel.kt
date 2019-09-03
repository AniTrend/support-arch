package co.anitrend.arch.core.viewmodel.contract

import androidx.lifecycle.LiveData
import co.anitrend.arch.domain.entities.NetworkState

/**
 * Contract for view models that aids relaying commands to an underlying repository
 *
 * @since v0.9.X
 */
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
     * Forwards queries for the repository to handle
     *
     * @param payload request data to be used by the repository
     */
    operator fun invoke(payload: P)

    /**
     * Checks if the live data stored in the repository has is not null
     *
     * @return [Boolean] true or false
     */
    fun hasModelData(): Boolean = model.value != null

    /**
     * Requests the repository to perform a retry operation
     */
    fun retry()

    /**
     * Requests the repository to perform a refreshAndInvalidate operation on the underlying database
     */
    fun refresh()
}