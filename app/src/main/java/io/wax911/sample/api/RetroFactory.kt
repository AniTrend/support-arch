package io.wax911.sample.api

import android.content.Context
import io.wax911.sample.api.interceptor.AuthInterceptor
import io.wax911.sample.api.interceptor.ClientInterceptor
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.wax911.sample.BuildConfig
import io.wax911.sample.util.Settings
import io.wax911.support.factory.SingletonCreator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetroFactory private constructor(context: Context) {

    val retrofit: Retrofit by lazy {
        val httpClient = createHttpClient(HttpLoggingInterceptor.Level.BODY,
            AuthInterceptor(Settings.newInstance(context), context))

        Retrofit.Builder().client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BuildConfig.API_URL)
                .build()
    }

    /**
     * Creates a standard HttpBuilder with most common likely used configuration and optionally
     * will include http logging based off a given log level.
     * @see HttpLoggingInterceptor.level
     * <br></br>
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

    companion object : SingletonCreator<RetroFactory, Context>({ RetroFactory(it) }) {
        val gson: Gson by lazy {
            GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .enableComplexMapKeySerialization()
                    .generateNonExecutableJson()
                    .setLenient()
                    .create()
        }
    }
}