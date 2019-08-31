package io.wax911.sample.extension

import android.content.Context
import co.anitrend.arch.core.analytic.contract.ISupportAnalytics
import io.wax911.sample.App


fun Context?.getAnalytics(): ISupportAnalytics? = this?.let {
    val application = it.applicationContext as App
    application.analyticsUtil
}