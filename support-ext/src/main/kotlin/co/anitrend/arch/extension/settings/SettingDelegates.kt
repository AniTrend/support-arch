package co.anitrend.arch.extension.settings

import android.content.SharedPreferences
import android.content.res.Resources
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.arch.extension.preference.*
import co.anitrend.arch.extension.settings.contract.AbstractSetting
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow

private typealias SettingsListener = SharedPreferences.OnSharedPreferenceChangeListener

/**
 * @since v1.3.0
 * @see AbstractSetting
 */
class EnumSetting<T : Enum<*>>(
    key: Int,
    default: T,
    resources: Resources,
    preference: SharedPreferences
) : AbstractSetting<T>(preference, default) {

    override val identifier = resources.getString(key)
    
    override var value by EnumPreference(identifier, default)
    
    override val flow by lazy(UNSAFE) {
        callbackFlow {
            val listener = SettingsListener { _, id ->
                if (id == identifier)
                    sendBlocking(value)
            }
            preference.registerOnSharedPreferenceChangeListener(listener)
            awaitClose { preference.unregisterOnSharedPreferenceChangeListener(listener) }
        }
    }
}

/**
 * @since v1.3.0
 * @see AbstractSetting
 */
class BooleanSetting(
    key: Int,
    default: Boolean,
    resources: Resources,
    preference: SharedPreferences
) : AbstractSetting<Boolean>(preference, default) {

    override val identifier = resources.getString(key)

    override var value by BooleanPreference(identifier, default)

    override val flow by lazy(UNSAFE) {
        callbackFlow {
            val listener = SettingsListener { _, id ->
                if (id == identifier)
                    sendBlocking(value)
            }
            preference.registerOnSharedPreferenceChangeListener(listener)
            awaitClose { preference.unregisterOnSharedPreferenceChangeListener(listener) }
        }
    }
}

/**
 * @since v1.3.0
 * @see AbstractSetting
 */
class IntSetting(
    key: Int,
    default: Int,
    resources: Resources,
    preference: SharedPreferences
) : AbstractSetting<Int>(preference, default) {

    override val identifier = resources.getString(key)

    override var value by IntPreference(identifier, default)

    override val flow by lazy(UNSAFE) {
        callbackFlow {
            val listener = SettingsListener { _, id ->
                if (id == identifier)
                    sendBlocking(value)
            }
            preference.registerOnSharedPreferenceChangeListener(listener)
            awaitClose { preference.unregisterOnSharedPreferenceChangeListener(listener) }
        }
    }
}

/**
 * @since v1.3.0
 * @see AbstractSetting
 */
class FloatSetting(
    key: Int,
    default: Float,
    resources: Resources,
    preference: SharedPreferences
) : AbstractSetting<Float>(preference, default) {

    override val identifier = resources.getString(key)

    override var value by FloatPreference(identifier, default)

    override val flow by lazy(UNSAFE) {
        callbackFlow {
            val listener = SettingsListener { _, id ->
                if (id == identifier)
                    sendBlocking(value)
            }
            preference.registerOnSharedPreferenceChangeListener(listener)
            awaitClose { preference.unregisterOnSharedPreferenceChangeListener(listener) }
        }
    }
}

/**
 * @since v1.3.0
 * @see AbstractSetting
 */
class LongSetting(
    key: Int,
    default: Long,
    resources: Resources,
    preference: SharedPreferences
) : AbstractSetting<Long>(preference, default) {

    override val identifier = resources.getString(key)

    override var value by LongPreference(identifier, default)

    override val flow by lazy(UNSAFE) {
        callbackFlow {
            val listener = SettingsListener { _, id ->
                if (id == identifier)
                    sendBlocking(value)
            }
            preference.registerOnSharedPreferenceChangeListener(listener)
            awaitClose { preference.unregisterOnSharedPreferenceChangeListener(listener) }
        }
    }
}

/**
 * @since v1.3.0
 * @see AbstractSetting
 */
class StringSetting(
    key: Int,
    default: String,
    resources: Resources,
    preference: SharedPreferences
) : AbstractSetting<String>(preference, default) {

    override val identifier = resources.getString(key)

    override var value by StringPreference(identifier, default)

    override val flow by lazy(UNSAFE) {
        callbackFlow {
            val listener = SettingsListener { _, id ->
                if (id == identifier)
                    sendBlocking(value)
            }
            preference.registerOnSharedPreferenceChangeListener(listener)
            awaitClose { preference.unregisterOnSharedPreferenceChangeListener(listener) }
        }
    }
}

/**
 * @since v1.3.0
 * @see AbstractSetting
 */
class NullableStringSetting(
    key: Int,
    default: String?,
    resources: Resources,
    preference: SharedPreferences
) : AbstractSetting<String?>(preference, default) {

    override val identifier = resources.getString(key)

    override var value by NullableStringPreference(identifier, default)

    override val flow by lazy(UNSAFE) {
        callbackFlow {
            val listener = SettingsListener { _, id ->
                if (id == identifier)
                    sendBlocking(value)
            }
            preference.registerOnSharedPreferenceChangeListener(listener)
            awaitClose { preference.unregisterOnSharedPreferenceChangeListener(listener) }
        }
    }
}
