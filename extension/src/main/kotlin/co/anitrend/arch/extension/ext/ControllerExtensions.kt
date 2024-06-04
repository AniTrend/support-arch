/**
 * Copyright 2021 AniTrend
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.anitrend.arch.extension.ext

import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.SavedStateHandle
import timber.log.Timber

/**
 * Lazy intent parameters for saved state handle
 *
 * @param default default value to use when key does not exist
 * @param key lookup key for the embedded item in the [SavedStateHandle.get]
 *
 * @return [Lazy] of the target type
 */
fun <T> SavedStateHandle.extra(
    key: String,
    default: () -> T,
): Lazy<T> =
    lazy(PUBLICATION) {
        try {
            if (contains(key)) {
                requireNotNull(get(key) as? T)
            } else {
                default()
            }
        } catch (e: Exception) {
            error(e)
        }
    }

/**
 * Lazy intent parameters for saved state handle
 *
 * @param key lookup key for the embedded item in the [SavedStateHandle.get]
 *
 * @return [Lazy] of the target type
 */
fun <T> SavedStateHandle.extra(key: String): Lazy<T?> = extra(key = key, default = { null })

/**
 * Lazy intent parameters for fragment activities
 *
 * @param default default value to use when key does not exist
 * @param key lookup key for the embedded item in the [FragmentActivity.getIntent]
 *
 * @return [Lazy] of the target type
 */
inline fun <reified T> FragmentActivity.extra(
    key: String,
    crossinline default: () -> T,
): Lazy<T> =
    lazy(PUBLICATION) {
        try {
            if (intent?.extras?.containsKey(key) == true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent?.extras?.getParcelable(key, T::class.java) as T
                } else {
                    @Suppress("DEPRECATION")
                    intent?.extras?.get(key) as T
                }
            } else {
                Timber.w("$this does not have an argument with key: $key")
                default()
            }
        } catch (e: Exception) {
            error(e)
        }
    }

/**
 * Lazy intent parameters for fragment activities
 *
 * @param key lookup key for the embedded item in the [FragmentActivity.getIntent]
 *
 * @return [Lazy] of the target type
 */
inline fun <reified T> FragmentActivity.extra(key: String): Lazy<T?> = extra(key = key, default = { null })

/**
 * Lazy intent parameters for fragments
 *
 * @param default default value to use when key does not exist
 * @param key lookup key for the embedded item in the [Fragment.getArguments]
 *
 * @return [Lazy] of the target type
 */
inline fun <reified T> Fragment.argument(
    crossinline default: () -> T,
    key: String,
): Lazy<T> =
    lazy(PUBLICATION) {
        try {
            if (arguments?.containsKey(key) == true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arguments?.getParcelable(key, T::class.java) as T
                } else {
                    @Suppress("DEPRECATION")
                    arguments?.get(key) as T
                }
            } else {
                Timber.w("$this does not have an argument with key: $key")
                default()
            }
        } catch (e: Exception) {
            error(e)
        }
    }

/**
 * Lazy intent parameters for fragments
 *
 * @param key lookup key for the embedded item in the [Fragment.getArguments]
 *
 * @return [Lazy] of the target type
 */
inline fun <reified T> Fragment.argument(key: String): Lazy<T?> = argument(key = key, default = { null })
