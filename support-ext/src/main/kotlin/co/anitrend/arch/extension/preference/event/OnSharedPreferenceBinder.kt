package co.anitrend.arch.extension.preference.event

import android.content.SharedPreferences

/**
 * Shared preference change listener binder
 *
 * @since v0.9.X
 */
@Deprecated(
    "Consider using flow to observe changes instead",
    replaceWith = ReplaceWith(
        "preferenceChangeFlow.collect",
        "co.anitrend.arch.extension.preference.SupportSettings.preferenceChangeFlow")
)
interface OnSharedPreferenceBinder {

    /**
     * Unregister any listeners from fragments or activities
     */
    fun onPause(changeListener: SharedPreferences.OnSharedPreferenceChangeListener)

    /**
     * Register any listeners from fragments or activities
     */
    fun onResume(changeListener: SharedPreferences.OnSharedPreferenceChangeListener)
}
