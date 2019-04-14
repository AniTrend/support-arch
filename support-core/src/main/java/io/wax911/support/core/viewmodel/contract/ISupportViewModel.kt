package io.wax911.support.core.viewmodel.contract

import android.content.Context

interface ISupportViewModel<M> {

    /**
     * Forwards queries for the repository to handle
     *
     * @see [io.wax911.support.core.repository.SupportRepository.requestFromNetwork]
     * @param context any valid application context
     */
    fun queryFor(context: Context?)

    /**
     * Checks if the live data stored in the repository has is not null
     *
     * @see [io.wax911.support.core.repository.SupportRepository.liveData]
     * @return [Boolean] true or false
     */
    fun hasModelData(): Boolean

    /**
     * Gets the current repository live data value
     *
     * @see [io.wax911.support.core.repository.SupportRepository.liveData]
     * @return [M] or null
     */
    fun getModelData(): M?

    /**
     * Sets the live data value, which will call [androidx.lifecycle.Observer.onChanged]
     *
     * @see [io.wax911.support.core.repository.SupportRepository.liveData]
     * @param data the variable to set to the repository live data
     */
    fun setModelData(data: M)
}