package co.anitrend.arch.extension.preference.event

import android.content.SharedPreferences

/**
 * Shared preference change listener binder
 *
 * @since v0.9.X
 */
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
