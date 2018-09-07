package io.wax911.support.custom.worker

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle

import retrofit2.Call
import retrofit2.Callback

abstract class SupportRequestEngine<T> : AsyncTask<Context, Void, Call<T>?>() {

    lateinit var bundle: Bundle
    protected lateinit var callback: Callback<T>

    abstract override fun doInBackground(vararg contexts: Context): Call<T>

    override fun onPostExecute(result: Call<T>?) {
        super.onPostExecute(result)
        if (!isCancelled)
            result?.enqueue(callback)
    }

    override fun onCancelled(result: Call<T>?) {
        super.onCancelled(result)
        result?.cancel()
    }
}
