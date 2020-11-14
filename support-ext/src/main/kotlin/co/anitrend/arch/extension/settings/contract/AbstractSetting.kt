package co.anitrend.arch.extension.settings.contract

import android.content.SharedPreferences
import android.content.res.Resources
import kotlinx.coroutines.flow.Flow

/**
 * An abstraction of a setting that is both observable and stateful
 *
 * @param preference Shared preference that will be used for persistence
 * @param default Default value to return
 */
abstract class AbstractSetting<T>(
    internal val preference: SharedPreferences,
    internal val default: T
) {
    abstract var value: T
    abstract val flow: Flow<T>
}