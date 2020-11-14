package co.anitrend.arch.extension.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import co.anitrend.arch.extension.preference.contract.ISupportPreference
import co.anitrend.arch.extension.settings.contract.AbstractSetting

/**
 * Core abstract implementation for application preferences
 *
 * @since v0.9.X
 *
 * @see AbstractSetting
 */
abstract class SupportPreference @JvmOverloads constructor(
    protected val context: Context,
    sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
) : ISupportPreference, SharedPreferences by sharedPreferences
