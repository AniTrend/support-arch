package io.wax911.sample.koin

import io.wax911.sample.analytics.AnalyticsLogger
import co.anitrend.arch.core.analytic.contract.ISupportAnalytics
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModules = module {
    factory<ISupportAnalytics> {
        AnalyticsLogger(
            context = androidContext()
        )
    }
}