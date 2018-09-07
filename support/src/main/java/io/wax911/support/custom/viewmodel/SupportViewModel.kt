package io.wax911.support.custom.viewmodel

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.wax911.support.base.dao.CrudRepository
import io.wax911.support.base.event.ResponseCallback
import io.wax911.support.base.event.RetroCallback
import io.wax911.support.custom.worker.SupportRequestEngine
import io.wax911.support.util.SupportUtil
import retrofit2.Call

abstract class SupportViewModel<M, K> : ViewModel(), RetroCallback<M> {

    var model : MutableLiveData<M> = MutableLiveData()
    val bundle by lazy { Bundle() }

    private lateinit var responseCallback: ResponseCallback<M>
    private lateinit var requestEngine: SupportRequestEngine<M>
    private lateinit var repository : CrudRepository<K, M>

    protected fun initDependencies(responseCallback: ResponseCallback<M>,
                                   requestEngine: SupportRequestEngine<M>,
                                   repository: CrudRepository<K, M>) {
        this.responseCallback = responseCallback
        this.requestEngine = requestEngine
        this.repository = repository
    }

    abstract fun requestData(requestType: Int, context: Context)

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     *
     * @param call the origination requesting object
     * @param throwable contains information about the error
     */
    override fun onFailure(call: Call<M>, throwable: Throwable) {
        throwable.printStackTrace()
        responseCallback.onResponseError(call, throwable)
    }

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     *
     *
     * It is useful when ViewModel observes some data and you need to clear this subscription to
     * prevent a leak of this ViewModel.
     */
    override fun onCleared() {
        super.onCleared()
        if (!SupportUtil.equals(requestEngine.status, AsyncTask.Status.FINISHED))
            requestEngine.cancel(true)
    }
}
