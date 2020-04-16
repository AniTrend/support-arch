package co.anitrend.arch.core.analytic.contract

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity

/**
 * Contract for analytics or crash reporting
 *
 * @since v1.0.X
 */
interface ISupportAnalytics {

    fun logCurrentScreen(context: FragmentActivity, tag: String)
    fun logCurrentState(tag: String, bundle: Bundle?)

    fun logException(throwable: Throwable)
    fun log(priority: Int = Log.VERBOSE, tag: String?, message: String)

    fun clearCrashAnalyticsSession()
    fun setCrashAnalyticIdentifier(identifier: String)
}
