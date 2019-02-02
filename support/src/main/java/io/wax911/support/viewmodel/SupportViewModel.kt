package io.wax911.support.custom.viewmodel

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModel
import io.wax911.support.custom.viewmodel.contract.ISupportViewModel
import io.wax911.support.repository.SupportRepository

abstract class SupportViewModel<M, K> : ViewModel(), ISupportViewModel<M> {

    val bundle by lazy { Bundle() }

    protected lateinit var repository : SupportRepository<K, M>

    /**
     * Forwards queries for the repository to handle
     *
     * @see [io.wax911.support.repository.SupportRepository.requestFromNetwork]
     * @param context any valid application context
     */
    override fun queryFor(context: Context?) = repository.requestFromNetwork(bundle, context)

    /**
     * Checks if the live data stored in the repository has is not null
     *
     * @return [Boolean] true or false
     */
    override fun hasModelData(): Boolean = repository.liveData.value != null

    override fun getModelData() : M? = repository.liveData.value

    /**
     * Sets the live data value
     */
    override fun setModelData(data: M) {
        repository.liveData.value = data
    }

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     *
     *
     * It is useful when ViewModel observes some data and you need to clear this subscription to
     * prevent a leak of this ViewModel.
     */
    override fun onCleared() {
        repository.onCleared()
        super.onCleared()
    }
}
