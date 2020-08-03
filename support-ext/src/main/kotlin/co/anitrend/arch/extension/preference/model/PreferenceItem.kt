package co.anitrend.arch.extension.preference.model

import android.content.SharedPreferences

/**
 * Model that represents a changed state for a preference
 *
 * @param key The key of the preference that has been changed
 * @param preference The preference that can be used to lookup against the [key]
 */
data class PreferenceItem(
    val key: String,
    val preference: SharedPreferences
)