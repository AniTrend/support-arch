package io.wax911.sample.extension

import android.content.Context
import io.wax911.sample.App
import io.wax911.support.core.analytic.contract.ISupportAnalytics


fun Context?.getAnalytics(): ISupportAnalytics? = this?.let {
    val application = it.applicationContext as App
    application.analyticsUtil
}