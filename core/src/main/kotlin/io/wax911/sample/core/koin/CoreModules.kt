package io.wax911.sample.core.koin

import co.anitrend.arch.core.analytic.contract.ISupportAnalytics
import co.anitrend.arch.extension.SupportDispatchers
import io.wax911.sample.core.analytics.AnalyticsLogger
import io.wax911.sample.core.presenter.CorePresenter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val coreModule = module {
    factory<ISupportAnalytics> {
        AnalyticsLogger(
            context = androidContext()
        )
    }

    single {
        SupportDispatchers()
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

val coreModules = listOf(
    coreModule, presenterModule
)