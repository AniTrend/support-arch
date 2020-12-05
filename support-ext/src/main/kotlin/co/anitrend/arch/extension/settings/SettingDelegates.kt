package co.anitrend.arch.extension.settings

import android.content.SharedPreferences
import android.content.res.Resources
import co.anitrend.arch.extension.preference.*
import co.anitrend.arch.extension.preference.BooleanPreference
import co.anitrend.arch.extension.preference.EnumPreference
import co.anitrend.arch.extension.preference.IntPreference
import co.anitrend.arch.extension.preference.LongPreference
import co.anitrend.arch.extension.preference.StringPreference
import co.anitrend.arch.extension.settings.contract.AbstractSetting
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

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

    override var value by EnumPreference(key, default, resources)

    override val flow = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener {
                _, _ -> sendBlocking(value)
        }
        preference.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { preference.unregisterOnSharedPreferenceChangeListener(listener) }
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

    override var value by BooleanPreference(key, default, resources)

    override val flow = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener {
                _, _ -> sendBlocking(value)
        }
        preference.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { preference.unregisterOnSharedPreferenceChangeListener(listener) }
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

    override var value by IntPreference(key, default, resources)

    override val flow = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener {
                _, _ -> sendBlocking(value)
        }
        preference.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { preference.unregisterOnSharedPreferenceChangeListener(listener) }
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

    override var value by FloatPreference(key, default, resources)

    override val flow = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener {
                _, _ -> sendBlocking(value)
        }
        preference.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { preference.unregisterOnSharedPreferenceChangeListener(listener) }
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

    override var value by LongPreference(key, default, resources)

    override val flow = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener {
                _, _ -> sendBlocking(value)
        }
        preference.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { preference.unregisterOnSharedPreferenceChangeListener(listener) }
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

    override var value by StringPreference(key, default, resources)

    override val flow = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener {
                _, _ -> sendBlocking(value)
        }
        preference.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { preference.unregisterOnSharedPreferenceChangeListener(listener) }
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

    override var value by NullableStringPreference(key, default, resources)

    override val flow = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener {
                _, _ -> sendBlocking(value)
        }
        preference.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { preference.unregisterOnSharedPreferenceChangeListener(listener) }
    }
}
