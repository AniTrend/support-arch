package io.wax911.support.core.viewmodel.contract

import androidx.lifecycle.LiveData
import io.wax911.support.data.model.NetworkState
import io.wax911.support.data.repository.SupportRepository

interface ISupportViewModel<M, P> {

    val model: LiveData<M?>

    val networkState: LiveData<NetworkState>?

    val refreshState: LiveData<NetworkState>?

    /**
     * Forwards queries for the repository to handle
     *
     * @see [io.wax911.support.data.repository.SupportRepository.invoke]
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