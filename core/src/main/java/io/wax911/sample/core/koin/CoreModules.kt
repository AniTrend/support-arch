package io.wax911.sample.core.koin

import io.wax911.sample.core.presenter.CorePresenter
import io.wax911.sample.data.usecase.meta.CountryFetchUseCase
import io.wax911.sample.data.usecase.meta.GenreFetchUseCase
import io.wax911.sample.data.usecase.meta.LanguageFetchUseCase
import io.wax911.sample.core.viewmodel.movie.MovieViewModel
import io.wax911.sample.core.viewmodel.show.ShowViewModel
import io.wax911.sample.data.dao.TraktTrendDatabase
import io.wax911.sample.data.repository.movie.MovieRepository
import io.wax911.sample.data.repository.show.ShowRepository
import io.wax911.support.data.factory.contract.IRetrofitFactory
import io.wax911.support.data.factory.contract.getEndPointOf
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val coreModules = module {
    factory {
        CountryFetchUseCase(
            metaEndpoints = get<IRetrofitFactory>().getEndPointOf(),
            countryDao = get<TraktTrendDatabase>().countryDao()
        )
    }
    factory {
        GenreFetchUseCase(
            metaEndpoints = get<IRetrofitFactory>().getEndPointOf(),
            genreDao = get<TraktTrendDatabase>().genreDao()
        )
    }
    factory {
        LanguageFetchUseCase(
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
            showRepository = get<ShowRepository>()
        )
    }
    viewModel {
        MovieViewModel(
            movieRepository = get<MovieRepository>()
        )
    }
}