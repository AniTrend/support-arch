package io.wax911.sample.util

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.google.firebase.analytics.FirebaseAnalytics
import io.fabric.sdk.android.BuildConfig
import io.fabric.sdk.android.Fabric
import io.wax911.support.util.SingletonUtil
import io.wax911.support.util.SupportAnalyticUtil
import io.wax911.support.empty

class AnalyticsUtil private constructor(context: Context) : SupportAnalyticUtil {

    init {
        configureAnalytics(context)
        configureCrashAnalytics(context)
    }

    private var analytics: FirebaseAnalytics? = null
    private var fabric: Fabric? = null

    /**
     * Sets application global fabric instance, depending on
     * the current application preferences the application may have
     * disabled the current instance from sending any data
     */
    private fun configureCrashAnalytics(context: Context) {
        when(!BuildConfig.DEBUG) {
            true -> {
                val crashlyticsCore = CrashlyticsCore.Builder()
                        .build()

                fabric = Fabric.with(Fabric.Builder(context)
                        .kits(crashlyticsCore)
                        .appIdentifier(BuildConfig.BUILD_TYPE)
                        .build())
            }
        }
    }

    /**
     * Application global firebase analytics
     */
    private fun configureAnalytics(context: Context) {
        when(!BuildConfig.DEBUG) {
            true -> {
                analytics = FirebaseAnalytics.getInstance(context)
                analytics!!.setAnalyticsCollectionEnabled(true)
                analytics!!.setMinimumSessionDuration(5000L)
            }
        }
    }

    override fun logCurrentScreen(fragmentActivity: FragmentActivity, fragmentActivityTag : String) {
        fabric?.currentActivity = fragmentActivity
        analytics?.setCurrentScreen(fragmentActivity, fragmentActivityTag, null)
    }

    override fun logCurrentState(tag: String, bundle: Bundle) {
        analytics?.logEvent(tag, bundle)
    }

    override fun logException(throwable: Throwable) =
        Crashlytics.logException(throwable)


    override fun log(tag: String, message: String) =
        Crashlytics.log(0, tag, message)

    override fun clearUserSession() =
        Crashlytics.setUserIdentifier(String.empty())

    override fun setCrashAnalyticUser(userName: String) {
        when (fabric != null) { true -> Crashlytics.setUserIdentifier(userName) }
    }

    override fun resetAnalyticsData() {
        analytics?.resetAnalyticsData()
    }

    companion object : SingletonUtil<SupportAnalyticUtil, Context> ({ AnalyticsUtil(it) })
}


