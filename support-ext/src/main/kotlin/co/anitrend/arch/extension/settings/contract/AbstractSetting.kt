package co.anitrend.arch.extension.settings.contract

import android.content.SharedPreferences
import android.content.res.Resources
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KProperty

/**
 * An abstraction of a setting that is both observable and stateful
 *
 * @param preference Shared preference that will be used for persistence
 * @param default Default value to return
 */
abstract class AbstractSetting<T>(
    internal val preference: SharedPreferences,
    internal val default: T
) {
    /**
     * Key identifier for the setting
     */
    protected abstract val identifier: String

    /**
     * Allow you to set a value for the setting or
     * access a snapshot value of the setting
     */
    protected abstract var value: T

    /**
     * Readonly observable to listen for changes for the setting
     */
    abstract val flow: Flow<T>

    /**
     * @param thisRef - the object for which the value is requested.
     * @param property - the metadata for the property.
     *
     * @return Snapshot of the setting
     */
    operator fun getValue(
        thisRef: AbstractSetting<T>,
        property: KProperty<*>
    ) = thisRef.value

    /**
     * Sets a value for the setting
     *
     * @param thisRef - the object for which the value is requested.
     * @param property - the metadata for the property.
     * @param value - the value to set.
     */
    operator fun setValue(
        thisRef: AbstractSetting<T>,
        property: KProperty<*>,
        value: T
    ) { thisRef.value = value }
}