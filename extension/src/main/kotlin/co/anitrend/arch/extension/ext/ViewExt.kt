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
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.ViewCompat
import co.anitrend.arch.extension.R
import co.anitrend.arch.extension.annotation.SupportExperimental
import com.google.android.material.snackbar.Snackbar

/**
 * Sets the current views visibility to GONE
 *
 * @see View.GONE
 */
fun View.gone() {
    visibility = View.GONE
}

/**
 * Sets the current views visibility to INVISIBLE
 *
 * @see View.INVISIBLE
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * Sets the current views visibility to VISIBLE
 *
 * @see View.VISIBLE
 */
fun View.visible() {
    visibility = View.VISIBLE
}

/**
 * Updates layout margins with the given values
 *
 * @throws UnsupportedOperationException if layoutParams is not a
 * type of [ViewGroup.MarginLayoutParams]
 */
@Throws(UnsupportedOperationException::class)
fun View.updateMargins(
    start: Int = 0,
    top: Int = 0,
    end: Int = 0,
    bottom: Int = 0
) {
    if (layoutParams !is ViewGroup.MarginLayoutParams)
        throw UnsupportedOperationException(
            "Expected layoutParams of ViewGroup.MarginLayoutParams but was $layoutParams instead"
        )
    val marginLayoutParams = layoutParams as ViewGroup.MarginLayoutParams
    marginLayoutParams.setMargins(start, top, end, bottom)
    requestLayout()
}

/**
 * Updates layout margins with the given values
 *
 * @throws UnsupportedOperationException if layoutParams is not a
 * type of [ViewGroup.MarginLayoutParams]
 */
@Throws(UnsupportedOperationException::class)
fun View.updateMargins(margin: Int) {
    updateMargins(margin, margin, margin, margin)
}

inline fun View.snackBar(
    text: String,
    duration: Int,
    actionText: String,
    crossinline action: (Snackbar) -> Unit
): Snackbar {
    val snackBar = Snackbar.make(this, text, duration)
    snackBar.setAction(actionText) { action(snackBar) }
    snackBar.view.setBackgroundColor(context.getColorFromAttr(R.attr.colorPrimaryDark))
    val mainTextView = snackBar.view.findViewById<TextView>(R.id.snackbar_text)
    val actionTextView = snackBar.view.findViewById<TextView>(R.id.snackbar_action)
    mainTextView.setTextColor(context.getColorFromAttr(R.attr.titleTextColor))
    actionTextView.setTextColor(context.getColorFromAttr(R.attr.colorAccent))
    actionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
    return snackBar
}

fun View.snackBar(
    @StringRes stringRes: Int,
    duration: Int,
    @StringRes actionRes: Int,
    action: (Snackbar) -> Unit
) = snackBar(
    resources.getString(stringRes),
    duration,
    resources.getString(actionRes),
    action
)

/**
 * Request to show or hide the soft input window
 *
 * @param show True if the keyboard should be shown otherwise False to hide it
 */
fun View.toggleIme(show: Boolean) {
    val controller = ViewCompat.getWindowInsetsController(this)
    when (show) {
        true -> controller?.show(WindowInsets.Type.ime())
        else -> controller?.hide(WindowInsets.Type.ime())
    }
}

/**
 * @return Visibility status of the [WindowInsets.Type.ime] or null
 */
fun View.isImeVisible(): Boolean? {
    val insets = ViewCompat.getRootWindowInsets(this)
    return insets?.isVisible(WindowInsets.Type.ime())
}

/**
 * @return Height of the [WindowInsets.Type.ime] or null if not shown
 */
fun View.imeHeight(): Int? {
    val insets = ViewCompat.getRootWindowInsets(this)
    return insets?.getInsets(WindowInsets.Type.ime())?.bottom
}

/**
 * @return Height of the [WindowInsets.Type.systemBars] top
 */
fun View.statusBarHeight(): Int? {
    val insets = ViewCompat.getRootWindowInsets(this)
    return insets?.getInsets(WindowInsets.Type.systemBars())?.top
}

/**
 * @return Height of the [WindowInsets.Type.systemBars] bottom
 */
fun View.navigationBarHeight(): Int? {
    val insets = ViewCompat.getRootWindowInsets(this)
    return insets?.getInsets(WindowInsets.Type.systemBars())?.bottom
}

/**
 * Gets status bar height, [credits](https://gist.github.com/hamakn/8939eb68a920a6d7a498)
 *
 * @return Height in pixels otherwise null if it cannot resolve
 *
 * @author hamakn
 */
@SupportExperimental
@Deprecated("Use View.statusBarHeight extension instead")
fun Resources.getStatusBarHeight(): Int? {
    val resourceId = getIdentifier(
        "status_bar_height",
        "dimen",
        "android"
    )
    if (resourceId > 0)
        return getDimensionPixelSize(resourceId)
    return null
}

/**
 * Gets status navigation bar height, [credits](https://gist.github.com/hamakn/8939eb68a920a6d7a498)
 *
 * @return Height in pixels otherwise null if it cannot resolve
 *
 * @author hamakn
 */
@SupportExperimental
@Deprecated("Use View.navigationBarHeight extension instead")
fun Resources.getNavigationBarHeight(): Int? {
    val resourceId = getIdentifier(
        "navigation_bar_height",
        "dimen",
        "android"
    )
    if (resourceId > 0)
        return getDimensionPixelSize(resourceId)
    return null
}
