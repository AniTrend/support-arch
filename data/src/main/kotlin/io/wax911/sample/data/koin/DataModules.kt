package io.wax911.sample.data.koin

import android.content.Context
import android.net.ConnectivityManager
import co.anitrend.arch.extension.util.SupportConnectivityHelper
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
}

val dataNetworkModules = module {
    single {
        Gson().newBuilder()
            .setLenient()
            .serializeNulls()
            .create()
    }

    factory {
        SupportConnectivityHelper(
            androidContext().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager?
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

val dataUseCaseModules = module {
    factory {
        ShowPagedListUseCase(
            showPagedRepository = get<ShowPagedRepository>()
        )
    }
    factory {
        MoviePagedListUseCase(
            moviePagedRepository = get<MoviePagedRepository>()
        )
    }
}

val dataSourceModules = module {
    factory {
        ShowPagedDataSource(
            showEndpoint = get<TraktV2>().shows(),
            showDao = get<TraktTrendDatabase>().showDao()
        )
    }
    factory {
        MoviePagedDataSource(
            movieEndpoint = get<TraktV2>().movies(),
            movieDao = get<TraktTrendDatabase>().movieDao()
        )
    }
}

val dataRepositoryModules = module {
    factory {
        ShowPagedRepository(
            showPagedDataSource = get<ShowPagedDataSource>()
        )
    }

    factory {
        MoviePagedRepository(
            moviePagedDataSource = get<MoviePagedDataSource>()
        )
    }
}