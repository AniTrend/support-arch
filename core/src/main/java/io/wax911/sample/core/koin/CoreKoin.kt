package io.wax911.sample.core.koin

import io.wax911.sample.core.api.NetworkClient
import io.wax911.sample.core.api.RetroFactory
import io.wax911.sample.core.auth.AuthenticationHelper
import io.wax911.sample.core.auth.contract.IAuthenticationHelper
import io.wax911.sample.core.dao.DatabaseHelper
import io.wax911.sample.core.presenter.CorePresenter
import io.wax911.sample.core.repository.movie.MovieRepository
import io.wax911.sample.core.repository.show.PopularShowRepository
import io.wax911.sample.core.util.Settings
import io.wax911.sample.core.viewmodel.movie.MovieViewModel
import io.wax911.sample.core.viewmodel.show.ShowViewModel
import io.wax911.support.core.controller.SupportRequestClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val coreModules = module {
    factory {
        Settings.newInstance(
            arg = androidContext()
        )
    }

    single {
        DatabaseHelper.newInstance(
            arg = androidContext()
        )
    }

    factory<IAuthenticationHelper> {
        AuthenticationHelper(
            databaseHelper = get()
        )
    }
}

val coreNetworkModules = module {
    factory<SupportRequestClient> {
        NetworkClient()
    }

    single {
        RetroFactory.newInstance(
            arg = androidContext()
        )
    }
}

val corePresenterModules = module {
    factory {
        CorePresenter(
            androidContext(),
            settings = get()
        )
    }
}

val coreViewModelModules = module {
    viewModel {
        ShowViewModel()
    }
    viewModel {
        MovieViewModel()
    }
}

val coreRepositoryModules = module {
    factory {
        PopularShowRepository(
            context = androidContext()
        )
    }
    factory {
        MovieRepository(
            context = androidContext()
        )
    }
}