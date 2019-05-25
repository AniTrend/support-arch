package io.wax911.sample.data.api

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.wax911.sample.data.BuildConfig
import io.wax911.sample.data.api.interceptor.AuthInterceptor
import io.wax911.sample.data.api.interceptor.ClientInterceptor
import io.wax911.support.core.factory.InstanceCreator
import io.wax911.support.core.factory.contract.IRetrofitFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Retrofit factory provides a Gson instance and creates endpoint services
 */
class RetroFactory private constructor(context: Context, baseUrl: String = BuildConfig.apiUrl) : IRetrofitFactory {

    private val retrofit: Retrofit by lazy {
        val httpClient = createHttpClient(
            HttpLoggingInterceptor.Level.BODY,
            AuthInterceptor(context)
        )

        Retrofit.Builder().client(httpClient.build())
            .addConverterFactory(
                GsonConverterFactory.create(gson)
            ).baseUrl(baseUrl).build()
    }

    /**
     * Creates a standard HttpBuilder with most common likely used configuration and optionally
     * will include http logging based off a given log level.
     * @see HttpLoggingInterceptor.level
     *
     * @param logLevel Mandatory log level that the logging http interceptor should use
     */
    private fun createHttpClient(logLevel: HttpLoggingInterceptor.Level, authInterceptor: AuthInterceptor): OkHttpClient.Builder {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .readTimeout(35, TimeUnit.SECONDS)
            .connectTimeout(35, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(ClientInterceptor())
            .addInterceptor(authInterceptor)
        when {
            BuildConfig.DEBUG -> {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                    .setLevel(logLevel)
                okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
            }
        }
        return okHttpClientBuilder
    }

    /**
     * Generates retrofit service classes
     *
     * @param serviceClass The interface class method representing your request to use
     */
    override fun <S> createService(serviceClass: Class<S>): S = retrofit.create(serviceClass)

    companion object : InstanceCreator<IRetrofitFactory, Context>({ RetroFactory(it) }) {
        val gson: Gson by lazy {
            GsonBuilder()
                .enableComplexMapKeySerialization()
                .generateNonExecutableJson()
                .serializeNulls()
                .setLenient()
                .create()
        }
    }
}