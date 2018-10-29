package io.wax911.sample.api

import io.wax911.support.custom.worker.SupportRequestClient
import io.wax911.support.extension.logError
import io.wax911.support.model.ModelWrapper
import io.wax911.support.util.InstanceUtilNoArg
import retrofit2.Call

class NetworkClient private constructor() : SupportRequestClient() {

    override fun <T> executeUsing(call: Call<T>): ModelWrapper<T?> {
        val response = call.execute()

        if (!response.isSuccessful)
            response.errorBody().logError()

        return ModelWrapper(response.code(),
                response.body(),
                response.headers(),
                response.errorBody())
    }

    companion object : InstanceUtilNoArg<NetworkClient>({ NetworkClient() })
}