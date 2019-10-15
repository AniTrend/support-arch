package co.anitrend.arch.extension.preference.contract

import android.content.SharedPreferences

/**
 * Default preference contract
 */
interface ISupportPreference {

    val sharedPreferences: SharedPreferences

    var isNewInstallation: Boolean
    var versionCode: Int

    companion object {
        internal const val VERSION_CODE = "_versionCode"
        internal const val IS_NEW_INSTALLATION = "_isNewInstallation"
    }
}