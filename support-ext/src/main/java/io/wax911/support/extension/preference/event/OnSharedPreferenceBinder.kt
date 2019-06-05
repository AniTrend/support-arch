package io.wax911.support.extension.preference.event

import android.content.SharedPreferences

/**
 * Created by max on 2017/06/14.
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
