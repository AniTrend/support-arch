package io.wax911.sample.data.koin

import android.content.Context
import android.net.ConnectivityManager
import io.wax911.sample.data.BuildConfig
import io.wax911.sample.data.api.endpoint.MovieEndpoint
import io.wax911.sample.data.api.endpoint.ShowEndpoint
import io.wax911.sample.data.api.endpoint.contract.TraktEndpointFactory.Companion.GSON
import io.wax911.sample.data.api.interceptor.AuthInterceptor
import io.wax911.sample.data.api.interceptor.ClientInterceptor
import io.wax911.sample.data.auth.AuthenticationHelper
import io.wax911.sample.data.dao.TraktTrendDatabase
import io.wax911.sample.data.repository.movie.MovieRepository
import io.wax911.sample.data.repository.show.ShowRepository
import io.wax911.sample.data.usecase.media.movie.MoviePagedListUseCase
import io.wax911.sample.data.usecase.media.show.ShowPagedListUseCase
import io.wax911.sample.data.util.Settings
import io.wax911.support.data.auth.contract.ISupportAuthentication
import io.wax911.support.extension.util.SupportConnectivityHelper
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val dataModules = module {
    factory {
        Settings(
            context = androidContext()
        )
    }

    single {
        TraktTrendDatabase.newInstance(
            context = androidContext()
        )
    }

    factory<ISupportAuthentication<Request.Builder>> {
        AuthenticationHelper(
            connectivityHelper = get(),
            jsonWebTokenDao = get<TraktTrendDatabase>().jsonTokenDao(),
            settings = get()
        )
    }
}

val dataNetworkModules = module {
    factory {
        AuthInterceptor(
            authenticationHelper = get()
        )
    }

    factory {
        SupportConnectivityHelper(
            androidContext().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager?
        )
    }

    single {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(ClientInterceptor())
            .authenticator(get<AuthInterceptor>())
        when {
            BuildConfig.DEBUG -> {
                val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.HEADERS
                }
                okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
            }
        }

        Retrofit.Builder().client(
            okHttpClientBuilder.build()
        ).addConverterFactory(
            GsonConverterFactory.create(
                GSON
            )
        ).baseUrl(
            BuildConfig.apiUrl
        ).build()
    }
}

val dataUseCaseModules = module {
    factory {
        ShowPagedListUseCase(
            showEndpoint = ShowEndpoint.create(),
            showDao = get<TraktTrendDatabase>().showDao()
        )
    }
    factory {
        MoviePagedListUseCase(
            movieEndpoint = MovieEndpoint.create(),
            movieDao = get<TraktTrendDatabase>().movieDao()
        )
    }
}

val dataRepositoryModules = module {
    factory {
        ShowRepository(
            showPagedListUseCase = get<ShowPagedListUseCase>()
        )
    }

    factory {
        MovieRepository(
            moviePagedListUseCase = get<MoviePagedListUseCase>()
        )
    }
}