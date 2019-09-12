package co.anitrend.arch.extension.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import co.anitrend.arch.extension.LAZY_MODE_UNSAFE

/**
 * Core abstract implementation for application preferences
 *
 * @since v0.9.X
 */
abstract class SupportPreference(protected val context: Context) {

    val sharedPreferences: SharedPreferences by lazy(LAZY_MODE_UNSAFE) {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    var isAuthenticated = false
        get() = sharedPreferences.getBoolean(IS_AUTHENTICATED, false)
        set(value) {
            field = value
            sharedPreferences.edit().putBoolean(IS_AUTHENTICATED, value).apply()
        }

    var isNewInstallation = true
        get() = sharedPreferences.getBoolean(IS_NEW_INSTALLATION, true)
        set(value) {
            field = value
            sharedPreferences.edit().putBoolean(IS_NEW_INSTALLATION, value).apply()
        }

    var versionCode = 1
        get() = sharedPreferences.getInt(VERSION_CODE, 1)
        set(value) {
            field = value
            sharedPreferences.edit().putInt(VERSION_CODE, value).apply()
        }

    companion object {
        const val VERSION_CODE = "_versionCode"
        const val IS_NEW_INSTALLATION = "_isNewInstallation"
        const val IS_AUTHENTICATED = "_isAuthenticated"
    }
}
