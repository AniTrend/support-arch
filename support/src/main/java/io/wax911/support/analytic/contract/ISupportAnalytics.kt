package io.wax911.support.analytic.contract

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

interface ISupportAnalytics {

    fun logCurrentScreen(context: FragmentActivity, tag: String)
    fun logCurrentState(tag: String, bundle: Bundle)
    fun logException(throwable: Throwable)
    fun log(tag: String, message: String)

    fun clearUserSession()
    fun setCrashAnalyticUser(userIdentifier: String)

    fun resetAnalyticsData()
}
