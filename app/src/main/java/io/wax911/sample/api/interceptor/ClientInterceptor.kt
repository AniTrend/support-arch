package io.wax911.sample.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class ClientInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()

        builder.apply {
            addHeader("Accept", "application/json")
            addHeader("Accept", "application/*+json")
        }

        return chain.proceed(builder.build())
    }
}