package io.wax911.sample.api

import android.content.Context
import android.util.Log
import io.wax911.sample.util.AnalyticsUtil
import io.wax911.support.custom.worker.SupportRequestClient
import io.wax911.support.model.ModelWrapper
import io.wax911.support.util.InstanceUtilNoArg
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Call

class NetworkClient private constructor() : SupportRequestClient() {

    override suspend fun <T> executeUsing(call: Call<T>, context: Context) : ModelWrapper<T?> {
        try {
            val response = call.execute()

            response.errorBody()?.also {
                if (!it.string().isNullOrEmpty()) {
                    Log.e(toString(), it.string())
                    AnalyticsUtil.getInstance(context)
                            .logException(Exception(it.string().toString()))
                }
            }
            return ModelWrapper(response.code(), response.body(), response.headers(), response.errorBody())
        } catch (e: Exception) {
            e.printStackTrace()
            return ModelWrapper(500, null, call.request().headers(),
                    ResponseBody.create(MediaType.parse("text/plain"), e.localizedMessage))
        }
    }

    companion object : InstanceUtilNoArg<NetworkClient>({ NetworkClient() })
}