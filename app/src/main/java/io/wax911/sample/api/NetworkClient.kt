package io.wax911.sample.api

import android.content.Context
import io.wax911.support.custom.worker.SupportRequestEngine
import retrofit2.Call
import retrofit2.Callback

class NetworkClient<M> private constructor() : SupportRequestEngine<M>() {

    override fun doInBackground(vararg contexts: Context): Call<M> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        fun <T> createInstance(callback: Callback<T>) : SupportRequestEngine<T> {
            val requestEngine = NetworkClient<T>()
            requestEngine.callback = callback
            return requestEngine
        }
    }
}