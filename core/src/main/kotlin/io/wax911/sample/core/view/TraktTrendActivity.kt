package io.wax911.sample.core.view

import android.os.Build
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import co.anitrend.arch.core.presenter.SupportPresenter
import co.anitrend.arch.extension.getCompatColor
import co.anitrend.arch.ui.activity.SupportActivity
import io.wax911.sample.core.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope


/**
 * Customizable activity for the application
 */
abstract class TraktTrendActivity<M, P : SupportPresenter<*>> : SupportActivity<M, P>() {

    /**
     * Can be used to configure custom theme styling as desired
     */
    override fun configureActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val systemUiOptions = window.decorView.systemUiVisibility
            when (AppCompatDelegate.getDefaultNightMode()) {
                AppCompatDelegate.MODE_NIGHT_NO -> {
                    window.navigationBarColor = getCompatColor(R.color.colorPrimary)
                    window.decorView.systemUiVisibility = systemUiOptions or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                }
                AppCompatDelegate.MODE_NIGHT_YES -> {
                    window.navigationBarColor = getCompatColor(R.color.colorPrimary)
                    window.decorView.systemUiVisibility = systemUiOptions and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    window.decorView.systemUiVisibility = systemUiOptions and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
                else -> {
                    // According to Google/IO other ui options like auto and follow system might be deprecated
                }
            }
        }
    }
}