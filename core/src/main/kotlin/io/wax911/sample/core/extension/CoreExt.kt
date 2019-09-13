package io.wax911.sample.core.extension

import co.anitrend.arch.core.analytic.contract.ISupportAnalytics
import org.koin.core.context.GlobalContext


val analytics by lazy {
    GlobalContext.get().koin.get<ISupportAnalytics>()
}