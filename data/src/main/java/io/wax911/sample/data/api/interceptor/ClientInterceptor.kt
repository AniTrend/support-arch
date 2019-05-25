package io.wax911.sample.data.api.interceptor

import io.wax911.sample.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ClientInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()

        builder.apply {
            addHeader("Content-Type", TRAKT_CONTENT_TYPE)
            addHeader("trakt-api-key", BuildConfig.clientId)
            addHeader("trakt-api-version", TRAKT_API_VERSION)
        }

        return chain.proceed(builder.build())
    }

    companion object {
        const val TRAKT_CONTENT_TYPE = "application/json"
        const val TRAKT_API_VERSION = "2"
    }
}