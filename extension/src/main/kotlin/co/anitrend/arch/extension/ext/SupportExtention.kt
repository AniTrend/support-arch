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

import android.content.res.Resources
import java.util.Locale
import kotlin.math.roundToInt

/**
 * No locks are used to synchronize an access to the [Lazy] instance value; if the instance is accessed from multiple threads,
 * its behavior is undefined.
 *
 * This mode should not be used unless the [Lazy] instance is guaranteed never to be initialized from more than one thread.
 */
val UNSAFE = LazyThreadSafetyMode.NONE

/**
 * Initializer function can be called several times on concurrent access to uninitialized [Lazy] instance value,
 * but only the first returned value will be used as the value of [Lazy] instance.
 */
val PUBLICATION = LazyThreadSafetyMode.PUBLICATION

/**
 * Locks are used to ensure that only a single thread can initialize the [Lazy] instance.
 */
val SYNCHRONIZED = LazyThreadSafetyMode.SYNCHRONIZED

/**
 * Potentially useless but returns an empty string, the signature may change in future
 *
 * @see String.isNullOrBlank
 */
fun String.Companion.empty() = ""

/**
 * Returns a copy of this strings having its first letter uppercase, or the original string,
 * if it's empty or already starts with an upper case letter.
 *
 * @param exceptions words or characters to exclude during capitalization
 */
fun String?.capitalizeWords(exceptions: List<String>? = null): String = when {
    !this.isNullOrEmpty() -> {
        val result = StringBuilder(length)
        val words = split("_|\\s".toRegex()).dropLastWhile { it.isEmpty() }
        for ((index, word) in words.withIndex()) {
            when (word.isNotEmpty()) {
                true -> {
                    if (!exceptions.isNullOrEmpty() && exceptions.contains(word))
                        result.append(word)
                    else result.append(
                        word.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        }
                    )
                }
                else -> {}
            }
            if (index != words.size - 1)
                result.append(" ")
        }
        result.toString()
    }
    else -> String.empty()
}

/**
 * Convert this receiver as DP to PX
 */
val Float.dipToPx: Int get() {
    val scale = Resources.getSystem().displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

/**
 * Convert this receiver as PX to DP
 */
val Float.pxToDip: Int get() {
    val scale = Resources.getSystem().displayMetrics.density
    return (this / scale + 0.5f).toInt()
}

/**
 * Convert this receiver as SP to PX
 */
val Float.spToPx: Int get() {
    val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
    return (this * scaledDensity).roundToInt()
}

/**
 * Check if this receiver is small width screen
 */
val Float.isSmallWidthScreen: Boolean get() {
    val displayMetrics = Resources.getSystem().displayMetrics
    val widthDp = displayMetrics.widthPixels / displayMetrics.density
    val heightDp = displayMetrics.heightPixels / displayMetrics.density
    val screenSw = widthDp.coerceAtMost(heightDp)
    return screenSw >= this
}

/**
 * Check if this receiver is wide screen
 */
val Float.isWideScreen: Boolean get() {
    val displayMetrics = Resources.getSystem().displayMetrics
    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    return screenWidth >= this
}
