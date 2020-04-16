package co.anitrend.arch.extension.preference

import android.content.SharedPreferences
import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.core.content.edit
import co.anitrend.arch.extension.preference.delegate.ISupportPreferenceDelegate
import co.anitrend.arch.extension.preference.delegate.ISupportPreferenceDelegate.Companion.stringOf
import kotlin.reflect.KProperty

/**
 * Enum preference delegate
 *
 * @see ISupportPreferenceDelegate
 */
class EnumPreference<T : Enum<*>>(
    @StringRes
    override val key: Int,
    override val default: T,
    override val resources: Resources
) : ISupportPreferenceDelegate<T> {

    override fun getValue(thisRef: SharedPreferences, property: KProperty<*>): T {
        val name = thisRef.getString(stringOf(key), default.name)
        val `class` = default::class.java
        return `class`.enumConstants?.firstOrNull { it.name == name } ?: default
    }

    override fun setValue(thisRef: SharedPreferences, property: KProperty<*>, value: T) {
        thisRef.edit {
            putString(stringOf(key), value.name)
        }
    }
}

/**
 * Boolean preference delegate
 *
 * @see ISupportPreferenceDelegate
 */
class BooleanPreference(
    override val key: Int,
    override val default: Boolean,
    override val resources: Resources
) : ISupportPreferenceDelegate<Boolean> {

    override fun getValue(thisRef: SharedPreferences, property: KProperty<*>): Boolean {
        return thisRef.getBoolean(stringOf(key), default)
    }

    override fun setValue(thisRef: SharedPreferences, property: KProperty<*>, value: Boolean) {
        thisRef.edit {
            putBoolean(stringOf(key), value)
        }
    }
}

/**
 * Int preference delegate
 *
 * @see ISupportPreferenceDelegate
 */
class IntPreference(
    override val key: Int,
    override val default: Int,
    override val resources: Resources
) : ISupportPreferenceDelegate<Int> {

    override fun getValue(thisRef: SharedPreferences, property: KProperty<*>): Int {
        return thisRef.getInt(stringOf(key), default)
    }

    override fun setValue(thisRef: SharedPreferences, property: KProperty<*>, value: Int) {
        thisRef.edit {
            putInt(stringOf(key), value)
        }
    }
}

/**
 * Long preference delegate
 *
 * @see ISupportPreferenceDelegate
 */
class LongPreference(
    override val key: Int,
    override val default: Long,
    override val resources: Resources
) : ISupportPreferenceDelegate<Long> {

    override fun getValue(thisRef: SharedPreferences, property: KProperty<*>): Long {
        return thisRef.getLong(stringOf(key), default)
    }

    override fun setValue(thisRef: SharedPreferences, property: KProperty<*>, value: Long) {
        thisRef.edit {
            putLong(stringOf(key), value)
        }
    }
}

/**
 * String preference delegate
 *
 * @see ISupportPreferenceDelegate
 */
class StringPreference(
    override val key: Int,
    override val default: String,
    override val resources: Resources
) : ISupportPreferenceDelegate<String> {

    override fun getValue(thisRef: SharedPreferences, property: KProperty<*>): String {
        return thisRef.getString(stringOf(key), default) ?: default
    }

    override fun setValue(thisRef: SharedPreferences, property: KProperty<*>, value: String) {
        thisRef.edit {
            putString(stringOf(key), value)
        }
    }
}