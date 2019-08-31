package co.anitrend.arch.core.viewmodel.contract

import androidx.lifecycle.LiveData
import co.anitrend.arch.domain.entities.NetworkState

/**
 * Contract for view models that aids relaying commands to an underlying repository
 */
interface ISupportViewModel<P, R> {

    val model: LiveData<R?>

    val networkState: LiveData<NetworkState>?

    val refreshState: LiveData<NetworkState>?

    /**
     * Forwards queries for the repository to handle
     *
     * @see [co.anitrend.arch.data.repository.SupportRepository.invoke]
     * @param parameter request data to be used by the repository
     */
    operator fun invoke(parameter: P)

    /**
     * Checks if the live data stored in the repository has is not null
     *
     * @return [Boolean] true or false
     */
    fun hasModelData(): Boolean = model.value != null

    /**
     * Returns the current request bundle, this is nullable
     */
    fun currentRequestParameter(): P?

    /**
     * Requests the repository to perform a retry operation
     */
    fun retry()

    /**
     * Requests the repository to perform a refresh operation on the underlying database
     */
    fun refresh()
}