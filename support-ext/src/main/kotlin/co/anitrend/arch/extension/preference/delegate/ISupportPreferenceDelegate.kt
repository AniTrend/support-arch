package co.anitrend.arch.extension.preference.delegate

import android.content.SharedPreferences
import android.content.res.Resources
import kotlin.properties.ReadWriteProperty

/**
 * Helper interface used for implementing property delegates of read-write properties in settings.
 *
 * @property key lookup key for the preference
 * @property default default value to return
 * @property resources required to resolve [key] string id
 *
 * @param T the type of the property value.
 *
 * @since v1.3.0
 */
interface ISupportPreferenceDelegate<T> : ReadWriteProperty<SharedPreferences, T> {
    val key: Int
    val default: T
    val resources: Resources

    companion object {
        internal fun Int.string(resources: Resources) = resources.getString(this)
    }
}