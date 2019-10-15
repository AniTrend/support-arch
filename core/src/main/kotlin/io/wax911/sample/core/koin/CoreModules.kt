package io.wax911.sample.core.koin

import co.anitrend.arch.core.analytic.contract.ISupportAnalytics
import io.wax911.sample.core.analytics.AnalyticsLogger
import io.wax911.sample.core.presenter.CorePresenter
import io.wax911.sample.core.viewmodel.movie.MovieViewModel
import io.wax911.sample.core.viewmodel.show.ShowViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val coreModule = module {
    factory<ISupportAnalytics> {
        AnalyticsLogger(
            context = androidContext()
        )
    }
}

private val presenterModule = module {
    factory {
        CorePresenter(
            androidContext(),
            settings = get()
        )
    }
}

private val viewModelModule = module {
    viewModel {
        ShowViewModel(
            useCase = get()
        )
    }
    viewModel {
        MovieViewModel(
            useCase = get()
        )
    }
}

val coreModules = listOf(
    coreModule, presenterModule, viewModelModule
)