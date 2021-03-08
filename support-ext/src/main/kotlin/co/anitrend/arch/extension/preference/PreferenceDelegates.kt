package co.anitrend.arch.extension.preference

import androidx.core.content.edit
import co.anitrend.arch.extension.preference.delegate.ISupportPreferenceDelegate
import co.anitrend.arch.extension.settings.contract.AbstractSetting
import kotlin.reflect.KProperty

/**
 * [Enum] preference delegate
 *
 * @see ISupportPreferenceDelegate
 */
internal class EnumPreference<T : Enum<*>>(
    override val key: String,
    override val default: T
) : ISupportPreferenceDelegate<T> {

    override fun getValue(thisRef: AbstractSetting<T>, property: KProperty<*>): T {
        val name = thisRef.preference.getString(key, default.name)
        val `class` = default::class.java
        return `class`.enumConstants?.firstOrNull { it.name == name } ?: default
    }

    override fun setValue(thisRef: AbstractSetting<T>, property: KProperty<*>, value: T) {
        thisRef.preference.edit {
            putString(key, value.name)
        }
    }
}

/**
 * [Boolean] preference delegate
 *
 * @see ISupportPreferenceDelegate
 */
internal class BooleanPreference(
    override val key: String,
    override val default: Boolean
) : ISupportPreferenceDelegate<Boolean> {

    override fun getValue(thisRef: AbstractSetting<Boolean>, property: KProperty<*>): Boolean {
        return thisRef.preference.getBoolean(key, default)
    }

    override fun setValue(thisRef: AbstractSetting<Boolean>, property: KProperty<*>, value: Boolean) {
        thisRef.preference.edit {
            putBoolean(key, value)
        }
    }
}

/**
 * [Int] preference delegate
 *
 * @see ISupportPreferenceDelegate
 */
internal class IntPreference(
    override val key: String,
    override val default: Int
) : ISupportPreferenceDelegate<Int> {

    override fun getValue(thisRef: AbstractSetting<Int>, property: KProperty<*>): Int {
        return thisRef.preference.getInt(key, default)
    }

    override fun setValue(thisRef: AbstractSetting<Int>, property: KProperty<*>, value: Int) {
        thisRef.preference.edit {
            putInt(key, value)
        }
    }
}

/**
 * [Float] preference delegate
 *
 * @see ISupportPreferenceDelegate
 */
internal class FloatPreference(
    override val key: String,
    override val default: Float
) : ISupportPreferenceDelegate<Float> {

    override fun getValue(thisRef: AbstractSetting<Float>, property: KProperty<*>): Float {
        return thisRef.preference.getFloat(key, default)
    }

    override fun setValue(thisRef: AbstractSetting<Float>, property: KProperty<*>, value: Float) {
        thisRef.preference.edit {
            putFloat(key, value)
        }
    }
}

/**
 * [Long] preference delegate
 *
 * @see ISupportPreferenceDelegate
 */
internal class LongPreference(
    override val key: String,
    override val default: Long
) : ISupportPreferenceDelegate<Long> {

    override fun getValue(thisRef: AbstractSetting<Long>, property: KProperty<*>): Long {
        return thisRef.preference.getLong(key, default)
    }

    override fun setValue(thisRef: AbstractSetting<Long>, property: KProperty<*>, value: Long) {
        thisRef.preference.edit {
            putLong(key, value)
        }
    }
}

/**
 * [String] preference delegate
 *
 * @see ISupportPreferenceDelegate
 */
internal class StringPreference(
    override val key: String,
    override val default: String
) : ISupportPreferenceDelegate<String> {

    override fun getValue(thisRef: AbstractSetting<String>, property: KProperty<*>): String {
        return thisRef.preference.getString(key, default) ?: default
    }

    override fun setValue(thisRef: AbstractSetting<String>, property: KProperty<*>, value: String) {
        thisRef.preference.edit {
            putString(key, value)
        }
    }
}

/**
 * Null [String] preference delegate
 *
 * @see ISupportPreferenceDelegate
 */
internal class NullableStringPreference(
    override val key: String,
    override val default: String? = null
) : ISupportPreferenceDelegate<String?> {

    override fun getValue(thisRef: AbstractSetting<String?>, property: KProperty<*>): String? {
        return thisRef.preference.getString(key, default)
    }

    override fun setValue(thisRef: AbstractSetting<String?>, property: KProperty<*>, value: String?) {
        thisRef.preference.edit {
            putString(key, value)
        }
    }
}