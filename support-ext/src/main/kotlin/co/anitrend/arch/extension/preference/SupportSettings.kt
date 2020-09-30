package co.anitrend.arch.extension.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import co.anitrend.arch.extension.preference.contract.ISupportSettings
import co.anitrend.arch.extension.preference.model.PreferenceItem
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Core abstract implementation for application preferences
 *
 * @since v0.9.X
 */
abstract class SupportSettings @JvmOverloads constructor(
    protected val context: Context,
    sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
) : ISupportSettings, SharedPreferences by sharedPreferences {

    /**
     * A flow that can be observed to provide changes on preferences as they happen
     *
     * @see kotlinx.coroutines.flow.callbackFlow
     * @see kotlinx.coroutines.flow.collect
     */
    val preferenceChangeFlow: Flow<PreferenceItem> =
        callbackFlow {
            val listener = SharedPreferences.OnSharedPreferenceChangeListener {
                    pref, key -> sendBlocking(PreferenceItem(key, pref))
            }
            registerOnSharedPreferenceChangeListener(listener)
            awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
        }
}
