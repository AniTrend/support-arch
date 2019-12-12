package io.wax911.sample.data.koin

import android.content.Context
import android.net.ConnectivityManager
import co.anitrend.arch.extension.SupportDispatchers
import co.anitrend.arch.extension.network.SupportConnectivity
import co.anitrend.arch.extension.systemServiceOf
import co.anitrend.arch.extension.util.date.SupportDateHelper
import com.google.gson.Gson
import com.uwetrottmann.trakt5.TraktV2
import io.wax911.sample.data.BuildConfig
import io.wax911.sample.data.datasource.local.TraktTrendDatabase
import io.wax911.sample.data.datasource.remote.movie.MoviePagedDataSource
import io.wax911.sample.data.datasource.remote.show.ShowPagedDataSource
import io.wax911.sample.data.repository.movie.MoviePagedRepository
import io.wax911.sample.data.repository.show.ShowPagedRepository
import io.wax911.sample.data.usecase.movie.MoviePagedListUseCase
import io.wax911.sample.data.usecase.show.ShowPagedListUseCase
import io.wax911.sample.data.util.Settings
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val dataModule = module {
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

    single {
        SupportDateHelper(
            context = androidContext()
        )
    }
}

private val networkModule = module {
    single {
        Gson().newBuilder()
            .setLenient()
            .serializeNulls()
            .create()
    }

    factory {
        SupportConnectivity(
            androidContext().systemServiceOf<ConnectivityManager>(
                Context.CONNECTIVITY_SERVICE
            )
        )
    }

    single {
        TraktV2(
            BuildConfig.clientId,
            BuildConfig.clientSecret,
            BuildConfig.redirectUri
        )
    }
}

private val useCaseModule = module {
    factory {
        ShowPagedListUseCase(
            repository = get()
        )
    }
    factory {
        MoviePagedListUseCase(
            repository = get()
        )
    }
}

private val sourceModule = module {
    factory {
        ShowPagedDataSource(
            showEndpoint = get<TraktV2>().shows(),
            showDao = get<TraktTrendDatabase>().showDao(),
            dispatchers = get()
        )
    }
    factory {
        MoviePagedDataSource(
            movieEndpoint = get<TraktV2>().movies(),
            movieDao = get<TraktTrendDatabase>().movieDao(),
            dispatchers = get()
        )
    }
}

private val repositoryModule = module {
    factory {
        ShowPagedRepository(
            source = get<ShowPagedDataSource>()
        )
    }

    factory {
        MoviePagedRepository(
            source = get<MoviePagedDataSource>()
        )
    }
}

val dataModules = listOf(
    dataModule, networkModule, useCaseModule, sourceModule, repositoryModule
)