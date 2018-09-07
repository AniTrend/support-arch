package io.wax911.support.util

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

abstract class SupportAnalytic {

    abstract fun logCurrentState(fragmentActivity: FragmentActivity, bundle: Bundle, tag: String)
    abstract fun reportException(tag: String, message: String)
    abstract fun clearSession()
    abstract fun setCrashAnalyticsUser(context: Context, userName: String)
}
