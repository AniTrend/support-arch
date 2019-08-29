package io.wax911.sample.koin

import io.wax911.sample.util.AnalyticsUtil
import io.wax911.support.core.analytic.contract.ISupportAnalytics
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModules = module {
    factory<ISupportAnalytics> {
        AnalyticsUtil(
            context = androidContext()
        )
    }
}