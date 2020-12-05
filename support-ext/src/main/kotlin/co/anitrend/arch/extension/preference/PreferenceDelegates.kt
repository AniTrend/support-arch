package co.anitrend.arch.extension.preference

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.core.content.edit
import co.anitrend.arch.extension.preference.delegate.ISupportPreferenceDelegate
import co.anitrend.arch.extension.preference.delegate.ISupportPreferenceDelegate.Companion.stringOf
import co.anitrend.arch.extension.settings.contract.AbstractSetting
import kotlin.reflect.KProperty

/**
 * [Enum] preference delegate
 *
 * @see ISupportPreferenceDelegate
 */
internal class EnumPreference<T : Enum<*>>(
    @StringRes
    override val key: Int,
    override val default: T,
    override val resources: Resources
) : ISupportPreferenceDelegate<T> {

    override fun getValue(thisRef: AbstractSetting<T>, property: KProperty<*>): T {
        val name = thisRef.preference.getString(stringOf(key), default.name)
        val `class` = default::class.java
        return `class`.enumConstants?.firstOrNull { it.name == name } ?: default
    }

    override fun setValue(thisRef: AbstractSetting<T>, property: KProperty<*>, value: T) {
        thisRef.preference.edit {
            putString(stringOf(key), value.name)
        }
    }
}

/**
 * [Boolean] preference delegate
 *
 * @see ISupportPreferenceDelegate
 */
internal class BooleanPreference(
    override val key: Int,
    override val default: Boolean,
    override val resources: Resources
) : ISupportPreferenceDelegate<Boolean> {

    override fun getValue(thisRef: AbstractSetting<Boolean>, property: KProperty<*>): Boolean {
        return thisRef.preference.getBoolean(stringOf(key), default)
    }

    override fun setValue(thisRef: AbstractSetting<Boolean>, property: KProperty<*>, value: Boolean) {
        thisRef.preference.edit {
            putBoolean(stringOf(key), value)
        }
    }
}

/**
 * [Int] preference delegate
 *
 * @see ISupportPreferenceDelegate
 */
internal class IntPreference(
    override val key: Int,
    override val default: Int,
    override val resources: Resources
) : ISupportPreferenceDelegate<Int> {

    override fun getValue(thisRef: AbstractSetting<Int>, property: KProperty<*>): Int {
        return thisRef.preference.getInt(stringOf(key), default)
    }

    override fun setValue(thisRef: AbstractSetting<Int>, property: KProperty<*>, value: Int) {
        thisRef.preference.edit {
            putInt(stringOf(key), value)
        }
    }
}

/**
 * [Float] preference delegate
 *
 * @see ISupportPreferenceDelegate
 */
internal class FloatPreference(
    override val key: Int,
    override val default: Float,
    override val resources: Resources
) : ISupportPreferenceDelegate<Float> {

    override fun getValue(thisRef: AbstractSetting<Float>, property: KProperty<*>): Float {
        return thisRef.preference.getFloat(stringOf(key), default)
    }

    override fun setValue(thisRef: AbstractSetting<Float>, property: KProperty<*>, value: Float) {
        thisRef.preference.edit {
            putFloat(stringOf(key), value)
        }
    }
}

/**
 * [Long] preference delegate
 *
 * @see ISupportPreferenceDelegate
 */
internal class LongPreference(
    override val key: Int,
    override val default: Long,
    override val resources: Resources
) : ISupportPreferenceDelegate<Long> {

    override fun getValue(thisRef: AbstractSetting<Long>, property: KProperty<*>): Long {
        return thisRef.preference.getLong(stringOf(key), default)
    }

    override fun setValue(thisRef: AbstractSetting<Long>, property: KProperty<*>, value: Long) {
        thisRef.preference.edit {
            putLong(stringOf(key), value)
        }
    }
}

/**
 * [String] preference delegate
 *
 * @see ISupportPreferenceDelegate
 */
internal class StringPreference(
    override val key: Int,
    override val default: String,
    override val resources: Resources
) : ISupportPreferenceDelegate<String> {

    override fun getValue(thisRef: AbstractSetting<String>, property: KProperty<*>): String {
        return thisRef.preference.getString(stringOf(key), default) ?: default
    }

    override fun setValue(thisRef: AbstractSetting<String>, property: KProperty<*>, value: String) {
        thisRef.preference.edit {
            putString(stringOf(key), value)
        }
    }
}

/**
 * Null [String] preference delegate
 *
 * @see ISupportPreferenceDelegate
 */
internal class NullableStringPreference(
    override val key: Int,
    override val default: String? = null,
    override val resources: Resources
) : ISupportPreferenceDelegate<String?> {

    override fun getValue(thisRef: AbstractSetting<String?>, property: KProperty<*>): String? {
        return thisRef.preference.getString(stringOf(key), default)
    }

    override fun setValue(thisRef: AbstractSetting<String?>, property: KProperty<*>, value: String?) {
        thisRef.preference.edit {
            putString(stringOf(key), value)
        }
    }
}