package io.wax911.support.base.event

import retrofit2.Call

/**
 * Created by max on 2017/10/15.
 * Callback for view mutableLiveData to communicate
 * with parent class or activity after a request
 */

interface ResponseCallback<T> {

    fun onResponseError(call: Call<T>, throwable: Throwable)

    fun onResponseSuccess(call: Call<T>, message: String)
}
