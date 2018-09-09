package io.wax911.support.custom.viewmodel

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import io.wax911.support.base.dao.CrudRepository

abstract class SupportViewModel<M, K> : ViewModel() {

    val bundle by lazy { Bundle() }

    protected lateinit var repository : CrudRepository<K, M>

    protected fun initDependencies(repository: CrudRepository<K, M>) {
        this.repository = repository
    }

    /**
     * Forwards queries to the repository
     *
     * @param requestType type of request
     * @param context any valid application context
     */
    fun queryFor(requestType: Int, context: Context) =
            repository.requestFromNetwork(requestType, bundle, context)

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     *
     *
     * It is useful when ViewModel observes some data and you need to clear this subscription to
     * prevent a leak of this ViewModel.
     */
    override fun onCleared() {
        super.onCleared()
        repository.onCleared()
    }

    fun observeOn(context: LifecycleOwner, observer: Observer<M>) =
        repository.registerObserver(context, observer)
}
