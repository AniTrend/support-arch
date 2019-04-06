package io.wax911.sample.util

import io.wax911.sample.BuildConfig
import io.wax911.sample.api.NetworkClient
import io.wax911.sample.api.RetroFactory
import io.wax911.sample.api.interceptor.ClientInterceptor
import io.wax911.sample.dao.WebTokenDao
import io.wax911.sample.model.WebToken
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

class AuthenticationUtil(private val webTokenDao: WebTokenDao?) {

    private val retrofit by lazy {
        val okHttpClientBuilder = OkHttpClient.Builder()
                .readTimeout(35, TimeUnit.SECONDS)
                .connectTimeout(35, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)

        when {
            BuildConfig.DEBUG -> {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
                        .addInterceptor(ClientInterceptor())
            }
        }

        Retrofit.Builder().client(okHttpClientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(RetroFactory.gson))
            .baseUrl(BuildConfig.API_URL)
            .build()
    }

    private val networkClient by lazy { NetworkClient() }

    @Synchronized fun getWebToken() : WebToken? {
        var webToken = webTokenDao?.get()
        when (webToken) {
            null -> Timber.e(toString(), "getWebToken -> returned null")
            else -> {
                if (webToken.hasExpired())
                    webToken = refreshToken(webToken)
            }
        }
        return webToken
    }

    private fun refreshToken(token : WebToken) : WebToken? {
        /*val call = retrofit.create(AuthEndpoint::class.java)
                .refreshToken(token.refresh_token)

        val wrapper = runBlocking {
            networkClient.executeUsingAsync(call).await()
        }
        val webTokenResponse = wrapper.model?.response
        wrapper.error?.logError()

        return webTokenResponse?.also {
            it.id = token.id
            it.calculateExpires()
            webTokenDao?.update(it)
        }*/
        return token.also {
            it.id = token.id
            it.calculateExpires()
            webTokenDao?.update(it)
        }
    }
}