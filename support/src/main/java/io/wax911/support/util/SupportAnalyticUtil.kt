package io.wax911.support.util

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

interface SupportAnalyticUtil {

    fun logCurrentScreen(fragmentActivity: FragmentActivity, fragmentActivityTag: String)
    fun logCurrentState(tag: String, bundle: Bundle)
    fun logException(throwable: Throwable)
    fun log(tag: String, message: String)

    fun clearUserSession()
    fun setCrashAnalyticUser(userName: String)

    fun resetAnalyticsData()
}
