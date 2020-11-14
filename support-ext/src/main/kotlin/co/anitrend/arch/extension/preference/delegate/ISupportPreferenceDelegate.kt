package co.anitrend.arch.extension.preference.delegate

import android.content.res.Resources
import co.anitrend.arch.extension.settings.contract.AbstractSetting
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
internal interface ISupportPreferenceDelegate<T> : ReadWriteProperty<AbstractSetting<T>, T> {
    val key: Int
    val default: T
    val resources: Resources

    companion object {
        internal fun ISupportPreferenceDelegate<*>.stringOf(key: Int) =
            resources.getString(key)
    }
}