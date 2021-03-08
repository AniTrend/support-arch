package co.anitrend.arch.extension.ext

import androidx.lifecycle.*

/**
 * Lazy intent parameters for saved state handle
 *
 * @param key lookup key for the embedded item in the [SavedStateHandle.get]
 * @param default default value to use when key does not exist
 *
 * @return [Lazy] of the target type
 */
fun <T : Any> SavedStateHandle.extra(key: String, default: T) = lazy(PUBLICATION) {
    try {
        if (contains(key))
            requireNotNull(get(key) as? T)
        else
            default
    } catch (e: Exception) {
        error(e)
    }
}