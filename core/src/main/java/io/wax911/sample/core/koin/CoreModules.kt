package io.wax911.sample.core.koin

import io.wax911.sample.core.presenter.CorePresenter
import io.wax911.sample.core.viewmodel.movie.MovieViewModel
import io.wax911.sample.core.viewmodel.show.ShowViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val coreModules = module {

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