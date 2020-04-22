package co.anitrend.arch.extension.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import co.anitrend.arch.extension.preference.contract.ISupportSettings

/**
 * Core abstract implementation for application preferences
 *
 * @since v0.9.X
 */
abstract class SupportSettings(
    protected val context: Context,
    private val sharedPreferences: SharedPreferences
) : ISupportSettings, SharedPreferences by sharedPreferences {

    constructor(context: Context): this(
        context, PreferenceManager.getDefaultSharedPreferences(context)
    )
}
