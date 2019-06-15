package io.wax911.sample.data.koin

import android.content.Context
import android.net.ConnectivityManager
import io.wax911.sample.data.api.RetroFactory
import io.wax911.sample.data.api.interceptor.AuthInterceptor
import io.wax911.sample.data.auth.AuthenticationHelper
import io.wax911.sample.data.dao.TraktTrendDatabase
import io.wax911.sample.data.repository.movie.MovieRepository
import io.wax911.sample.data.repository.show.ShowRepository
import io.wax911.sample.data.usecase.media.movie.MoviePagedListUseCase
import io.wax911.sample.data.usecase.media.show.ShowPagedListUseCase
import io.wax911.sample.data.util.Settings
import io.wax911.support.data.auth.contract.ISupportAuthentication
import io.wax911.support.data.factory.contract.IRetrofitFactory
import io.wax911.support.data.factory.contract.getEndPointOf
import io.wax911.support.extension.util.SupportConnectivityHelper
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

    factory<ISupportAuthentication> {
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

    single<IRetrofitFactory> {
        RetroFactory(
            authInterceptor = get<AuthInterceptor>()
        )
    }
}

val dataUseCaseModules = module {
    factory {
        ShowPagedListUseCase(
            showEndpoint = get<IRetrofitFactory>().getEndPointOf(),
            showDao = get<TraktTrendDatabase>().showDao()
        )
    }
    factory {
        MoviePagedListUseCase(
            movieEndpoint = get<IRetrofitFactory>().getEndPointOf(),
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