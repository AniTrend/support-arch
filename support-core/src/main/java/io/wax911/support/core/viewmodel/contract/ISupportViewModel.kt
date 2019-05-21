package io.wax911.support.core.viewmodel.contract

import android.os.Bundle
import androidx.lifecycle.LiveData
import io.wax911.support.core.repository.SupportRepository
import io.wax911.support.core.view.model.NetworkState

interface ISupportViewModel<M> {

    val repository : SupportRepository<M, *>

    val model: LiveData<M?>

    val networkState: LiveData<NetworkState>?

    val refreshState: LiveData<NetworkState>?

    /**
     * Forwards queries for the repository to handle
     *
     * @see [io.wax911.support.core.repository.SupportRepository.invokeRequest]
     * @param bundle request data to be used by the repository
     */
    fun queryFor(bundle: Bundle)

    /**
     * Checks if the live data stored in the repository has is not null
     *
     * @return [Boolean] true or false
     */
    fun hasModelData(): Boolean = model.value != null

    /**
     * Returns the current request bundle, this is nullable
     */
    fun currentRequestBundle(): Bundle?

    /**
     * Requests the repository to perform a retry operation
     */
    fun retry()

    /**
     * Requests the repository to perform a refresh operation on the underlying database
     */
    fun refresh()
}