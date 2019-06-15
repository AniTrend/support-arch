package io.wax911.sample.core.koin

import io.wax911.sample.core.presenter.CorePresenter
import io.wax911.sample.core.usecase.meta.CountryWorkerUseCase
import io.wax911.sample.core.usecase.meta.GenreWorkerUseCase
import io.wax911.sample.core.usecase.meta.LanguageWorkerUseCase
import io.wax911.sample.core.viewmodel.movie.MovieViewModel
import io.wax911.sample.core.viewmodel.show.ShowViewModel
import io.wax911.sample.data.dao.TraktTrendDatabase
import io.wax911.sample.data.repository.show.ShowRepository
import io.wax911.support.data.factory.contract.IRetrofitFactory
import io.wax911.support.data.factory.contract.getEndPointOf
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val coreModules = module {
    factory {
        CountryWorkerUseCase(
            metaEndpoints = get<IRetrofitFactory>().getEndPointOf(),
            countryDao = get<TraktTrendDatabase>().countryDao()
        )
    }
    factory {
        GenreWorkerUseCase(
            metaEndpoints = get<IRetrofitFactory>().getEndPointOf(),
            genreDao = get<TraktTrendDatabase>().genreDao()
        )
    }
    factory {
        LanguageWorkerUseCase(
            metaEndpoints = get<IRetrofitFactory>().getEndPointOf(),
            languageDao = get<TraktTrendDatabase>().languageDao()
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
        ShowViewModel(
            showRepository = get()
        )
    }
    viewModel {
        MovieViewModel(
            movieRepository = get()
        )
    }
}