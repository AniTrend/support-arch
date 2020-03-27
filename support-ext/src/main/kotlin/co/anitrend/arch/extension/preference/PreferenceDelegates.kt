/*
 * Copyright (C) 2020  AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package co.anitrend.arch.extension.preference

import android.content.SharedPreferences
import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.core.content.edit
import co.anitrend.arch.extension.preference.delegate.ISupportPreferenceDelegate
import co.anitrend.arch.extension.preference.delegate.ISupportPreferenceDelegate.Companion.string
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
        val name = thisRef.getString(key.string(resources), default.name)
        val `class` = default::class.java
        return `class`.enumConstants?.firstOrNull { it.name == name } ?: default
    }

    override fun setValue(thisRef: SharedPreferences, property: KProperty<*>, value: T) {
        thisRef.edit {
            putString(key.string(resources), value.name)
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
        return thisRef.getBoolean(key.string(resources), default)
    }

    override fun setValue(thisRef: SharedPreferences, property: KProperty<*>, value: Boolean) {
        thisRef.edit {
            putBoolean(key.string(resources), value)
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
        return thisRef.getInt(key.string(resources), default)
    }

    override fun setValue(thisRef: SharedPreferences, property: KProperty<*>, value: Int) {
        thisRef.edit {
            putInt(key.string(resources), value)
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
        return thisRef.getLong(key.string(resources), default)
    }

    override fun setValue(thisRef: SharedPreferences, property: KProperty<*>, value: Long) {
        thisRef.edit {
            putLong(key.string(resources), value)
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
        return thisRef.getString(key.string(resources), default) ?: default
    }

    override fun setValue(thisRef: SharedPreferences, property: KProperty<*>, value: String) {
        thisRef.edit {
            putString(key.string(resources), value)
        }
    }
}