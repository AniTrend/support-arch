package io.wax911.sample.api

import android.content.Context
import android.os.Bundle
import io.wax911.support.custom.worker.SupportRequestClient
import retrofit2.Call
import retrofit2.Callback

class NetworkClient<M> private constructor() : SupportRequestClient<M>() {

    override fun doInBackground(vararg contexts: Context): Call<M> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        fun <T> newInstance(callback: Callback<T>, parameters: Bundle) : SupportRequestClient<T> {
            val requestEngine = NetworkClient<T>()
            requestEngine.callback = callback
            requestEngine.parameters = parameters
            return requestEngine
        }
    }
}