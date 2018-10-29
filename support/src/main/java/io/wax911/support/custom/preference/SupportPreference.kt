package io.wax911.support.custom.preference

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.annotation.StyleRes
import io.wax911.support.R
import io.wax911.support.swapTheme

abstract class SupportPreference constructor(context: Context) {

    /** Base Application Values  */
    private val _versionCode = "_versionCode"
    private val _freshInstall = "_freshInstall"
    private val _isAuthenticated = "_isAuthenticated"
    private val _isLightTheme = "_isLightTheme"

    val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    var isAuthenticated = false
        get() = sharedPreferences.getBoolean(_isAuthenticated, false)
        set(value) {
            field = value
            sharedPreferences.edit().putBoolean(_isAuthenticated, value).apply()
        }

    var isfreshInstall = true
        get() = sharedPreferences.getBoolean(_freshInstall, true)
        set(value) {
            field = value
            sharedPreferences.edit().putBoolean(_freshInstall, value).apply()
        }

    var versionCode = 1
        get() = sharedPreferences.getInt(_versionCode, 1)
        set(value) {
            field = value
            sharedPreferences.edit().putInt(_versionCode, value).apply()
        }

    fun toggleTheme() {
        val editor = sharedPreferences.edit()
        editor.putInt(_isLightTheme, getTheme().swapTheme())
        editor.apply()
    }

    @StyleRes
    open fun getTheme(): Int = sharedPreferences.getInt(_isLightTheme, R.style.SupportThemeLight)
}
