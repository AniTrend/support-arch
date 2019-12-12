package io.wax911.sample.koin

import co.anitrend.arch.core.provider.SupportFileProvider
import io.wax911.sample.core.koin.coreModules
import io.wax911.sample.data.koin.dataModules
import io.wax911.sample.movie.viewmodel.MovieViewModel
import io.wax911.sample.show.viewmodel.ShowViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val appModule = module {
    factory {
        SupportFileProvider()
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

val appModules = listOf(
    appModule, viewModelModule
) + coreModules + dataModules