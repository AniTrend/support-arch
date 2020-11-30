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
import kotlin.math.roundToInt


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
 * set margin top for view
 */
fun View.setMarginTop(marginTop: Int) {
    val menuLayoutParams = layoutParams as ViewGroup.MarginLayoutParams
    menuLayoutParams.setMargins(0, marginTop, 0, 0)
    layoutParams = menuLayoutParams
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
fun View.isImeHeight(): Int? {
    val insets = ViewCompat.getRootWindowInsets(this)
    return insets?.getInsets(WindowInsets.Type.ime())?.bottom
}

fun Float.dipToPx() : Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

fun Float.pxToDip() : Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (this / scale + 0.5f).toInt()
}

fun Float.spToPx() : Int {
    val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
    return (this * scaledDensity).roundToInt()
}

fun Float.isSmallWidthScreen() : Boolean {
    val displayMetrics = Resources.getSystem().displayMetrics
    val widthDp = displayMetrics.widthPixels / displayMetrics.density
    val heightDp = displayMetrics.heightPixels / displayMetrics.density
    val screenSw = Math.min(widthDp, heightDp)
    return screenSw >= this
}

fun Float.isWideScreen() : Boolean {
    val displayMetrics = Resources.getSystem().displayMetrics
    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    return screenWidth >= this
}

/**
 * Gets status bar height, [credits](https://gist.github.com/hamakn/8939eb68a920a6d7a498)
 *
 * @return Height in pixels otherwise null if it cannot resolve
 *
 * @author hamakn
 */
@SupportExperimental
fun Resources.getStatusBarHeight() : Int? {
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
fun Resources.getNavigationBarHeight() : Int? {
    val resourceId = getIdentifier(
        "navigation_bar_height",
        "dimen",
        "android"
    )
    if (resourceId > 0)
        return getDimensionPixelSize(resourceId)
    return null
}