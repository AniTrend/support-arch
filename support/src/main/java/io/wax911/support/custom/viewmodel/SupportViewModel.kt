package io.wax911.support.custom.viewmodel

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModel
import io.wax911.support.base.dao.SupportRepository

abstract class SupportViewModel<M, K> : ViewModel() {

    val bundle by lazy { Bundle() }

    protected lateinit var repository : SupportRepository<K, M>

    /**
     * Forwards queries to the repository
     *
     * @param context any valid application context
     */
    fun queryFor(context: Context?) = repository.requestFromNetwork(bundle, context)

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
