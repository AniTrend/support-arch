package co.anitrend.arch.extension.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import co.anitrend.arch.extension.preference.contract.ISupportSettings
import co.anitrend.arch.extension.preference.model.PreferenceItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

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

    /**
     * A flow that can be observed to provide changes on preferences as they happen
     *
     * @see kotlinx.coroutines.flow.callbackFlow
     * @see kotlinx.coroutines.flow.collect
     */
    @ExperimentalCoroutinesApi
    val preferenceChangeFlow: Flow<PreferenceItem> =
        callbackFlow {
            val listener = SharedPreferences.OnSharedPreferenceChangeListener {
                    pref, key -> sendBlocking(PreferenceItem(key, pref))
            }
            registerOnSharedPreferenceChangeListener(listener)
            awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
        }
}
