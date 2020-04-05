package co.anitrend.arch.theme.extensions

import android.content.Context
import co.anitrend.arch.theme.R

/**
 * Check if the system is in night mode
 */
fun Context.isEnvironmentNightMode()=
    resources.getBoolean(R.bool.isNightMode)

/**
 * Check if the system should use light status bar
 */
fun Context.isLightStatusBar()=
    resources.getBoolean(R.bool.isLightStatusBar)

/**
 * Check if the system should use light navigation bar
 */
fun Context.isLightNavigationBar()=
    resources.getBoolean(R.bool.isLightNavigationBar)