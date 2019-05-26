package io.wax911.sample.data.koin

import android.content.Context
import android.net.ConnectivityManager
import io.wax911.sample.data.api.NetworkClient
import io.wax911.sample.data.api.RetroFactory
import io.wax911.sample.data.auth.AuthenticationHelper
import io.wax911.sample.data.auth.contract.IAuthenticationHelper
import io.wax911.sample.data.dao.DatabaseHelper
import io.wax911.sample.data.repository.movie.MovieRepository
import io.wax911.sample.data.repository.show.ShowRepository
import io.wax911.sample.data.util.Settings
import io.wax911.support.data.controller.contract.ISupportRequestClient
import io.wax911.support.data.factory.contract.IRetrofitFactory
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
        DatabaseHelper.newInstance(
            context = androidContext()
        )
    }

    factory<IAuthenticationHelper> {
        AuthenticationHelper(
            databaseHelper = get()
        )
    }
}

val dataNetworkModules = module {
    factory<ISupportRequestClient> {
        NetworkClient()
    }

    factory {
        SupportConnectivityHelper(
            androidContext().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager?
        )
    }

    single<IRetrofitFactory> {
        RetroFactory()
    }
}

val dataRepositoryModules = module {
    factory {
        ShowRepository()
    }

    factory {
        MovieRepository()
    }
}