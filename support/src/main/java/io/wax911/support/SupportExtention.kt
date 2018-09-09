package io.wax911.support

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityManagerCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import com.annimon.stream.IntPair
import com.annimon.stream.Objects
import com.annimon.stream.Optional
import com.annimon.stream.Stream
import io.wax911.support.custom.presenter.SupportPresenter
import okhttp3.Cache
import java.io.File
import java.util.*

object ComparatorUtil {
    fun <T> getKeyComparator(): Comparator<Map.Entry<String, T>> =
            kotlin.Comparator { o1, o2 -> o1.key.compareTo(o2.key)  }
}

@StyleRes
fun Int.swapTheme() : Int = when (this == R.style.SupportThemeLight) {
    true -> R.style.SupportThemeDark
    false -> R.style.SupportThemeLight
}

fun String.Companion.empty(): String =
        ""

fun FragmentActivity.hideKeyboard() {
    val inputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.window.decorView.windowToken, 0)
}

fun Context.isLowRamDevice() : Boolean {
    val activityManager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    return ActivityManagerCompat.isLowRamDevice(activityManager)
}

fun Context.getConnectivityManager() : ConnectivityManager =
    this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


fun Context.isConnectedToNetwork() : Boolean {
    val connectivityManager = this.getConnectivityManager()
    return connectivityManager.activeNetworkInfo.isConnected
}

fun Context.getOkHttpCache(cacheLimit: Long) : Cache {
    val cacheDirectory = File(this.cacheDir, "response-cache")
    return Cache(cacheDirectory, cacheLimit)
}

/**
 * Returns a color from a defined attribute
 *
 * @param colorAttr Type of attribute resource
 *
 * @return Color Integer
 */
fun Context.getColorFromAttr(@AttrRes colorAttr : Int) : Int {
    val colorAttribute = this.obtainStyledAttributes(intArrayOf(colorAttr))
    @ColorInt val color = colorAttribute.getColor(0, 0)
    colorAttribute.recycle()
    return color
}

fun Context.getCompatColor(@ColorRes colorRes: Int) =
        ContextCompat.getColor(this, colorRes)

/**
 * Avoids resource not found when using vector drawables in API levels < Lollipop
 *
 * @param resource The resource id of the drawable or vector drawable
 *                 @see DrawableRes
 *
 * @return Drawable
 * @see Drawable
 */
fun Context.getCompatDrawable(@DrawableRes resource : Int) : Drawable? =
        AppCompatResources.getDrawable(this, resource)

/**
 * Avoids resource not found when using vector drawables in API levels < Lollipop
 * Also images loaded from this method apply the {@link Drawable#mutate()} to assure
 * that the state of each drawable is not shared
 *
 * @param resource The resource id of the drawable or vector drawable
 *                 @see DrawableRes
 *
 * @param tint A specific color to tint the drawable
 *
 * @return Drawable
 * @see Drawable
 */
fun Context.getCompatDrawable(@DrawableRes resource : Int, @ColorRes tintColor : Int) : Drawable? {
    val drawableResource = AppCompatResources.getDrawable(this, resource)
    if (drawableResource != null) {
        val drawableResult = DrawableCompat.wrap(drawableResource).mutate()
        if (tintColor != 0)
            DrawableCompat.setTint(drawableResult, this.getCompatColor(tintColor))
        return drawableResource
    }
    return null
}

/**
 * Avoids resource not found when using vector drawables in API levels < Lollipop
 * and tints the drawable depending on the current selected theme, images loaded
 * from this method apply the {@link Drawable#mutate()} to assure that the state
 * of each drawable is not shared
 *
 * @param resource The resource id of the drawable or vector drawable
 *                 @see DrawableRes
 *
 * @return Drawable tinted with R.attr.titleColor
 * @see Drawable
 */
fun Context.getTintedDrawable(@DrawableRes resource : Int) : Drawable? {
    val drawable = DrawableCompat.wrap(Objects.requireNonNull(AppCompatResources.getDrawable(this, resource)!!)).mutate()
    DrawableCompat.setTint(drawable, this.getColorFromAttr(R.attr.titleColor))
    return drawable
}

/**
 * Avoids resource not found when using vector drawables in API levels < Lollipop
 * and tints the drawable depending on the current selected theme, images loaded
 * from this method apply the {@link Drawable#mutate()} to assure that the state
 * of each drawable is not shared
 *
 * @param resource The resource id of the drawable or vector drawable
 *                 @see DrawableRes
 *
 * @param colorAttr Type of attribute resource
 *
 * @return Drawable tinted with R.attr.titleColor
 * @see Drawable
 */
fun Context.getTintedDrawable(@DrawableRes resource : Int, @AttrRes colorAttr : Int) : Drawable? {
    val drawable = DrawableCompat.wrap(Objects.requireNonNull(AppCompatResources.getDrawable(this, resource)!!)).mutate()
    DrawableCompat.setTint(drawable, this.getColorFromAttr(colorAttr))
    return drawable
}

/**
 * Get screen dimensions for the current device configuration
 */
fun Context.getScreenDimens() : Point {
    val deviceDimens = Point()
    val windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    windowManager.defaultDisplay?.getSize(deviceDimens)
    return deviceDimens
}

/**
 * Starts a shared transition of activities connected by views
 * <br/>
 *
 * @param target The view from the calling activity with transition name
 * @param data Intent with bundle and or activity to start
 */
fun FragmentActivity.startSharedTransitionActivity(target : View, data : Intent) {
    val participants = Pair(target, ViewCompat.getTransitionName(target))
    val transitionActivityOptions = ActivityOptionsCompat
            .makeSceneTransitionAnimation(this, participants)
    ActivityCompat.startActivity(this, data, transitionActivityOptions.toBundle())
}


fun Int.isLightTheme() : Boolean {
    return this == R.style.SupportThemeLight
}

fun Context.isLightTheme(presenter: SupportPresenter<*>) : Boolean {
    return presenter.supportPreference.getTheme().isLightTheme()
}

fun Context.getLayoutInflater() : LayoutInflater =
        this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

/**
 * Credits
 * @author hamakn
 * https://gist.github.com/hamakn/8939eb68a920a6d7a498
 * */
fun Resources.getStatusBarHeight() : Int {
    var statusBarHeight = 0
    val resourceId = this.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0)
        statusBarHeight = this.getDimensionPixelSize(resourceId)
    return statusBarHeight
}

/**
 * Credits
 * @author hamakn
 * https://gist.github.com/hamakn/8939eb68a920a6d7a498
 * */
fun FragmentActivity.getActionBarHeight() : Int {
    val styledAttributes = this.theme.obtainStyledAttributes(
            intArrayOf(android.R.attr.actionBarSize)
    )
    styledAttributes.recycle()
    return styledAttributes.getDimension(0, 0f).toInt()
}

/**
 * Get List from a given array res
 * @return list of the array
 */
fun Context.getStringList(@ArrayRes arrayRes : Int) : List<String> {
    val array = this.resources.getStringArray(arrayRes)
    return array.constructListFrom<String>()
}

/**
 * Get List from a given array type
 * @return list of the array
 */
fun <T> Array<T>.constructListFrom() : List<T> = Arrays.asList(*this)

/**
 * Gets the index of any type of iterable guaranteed that an equal override for the class
 * of type T is implemented.
 *
 * @param targetItem the item to search
 * @return 0 if no result was found
 */
fun <A> Iterable<A>?.indexOf(targetItem: A?): Int = when (this != null) {
    targetItem != null -> {
        val pairOptional = Stream.of(this)
                .findIndexed { _, value -> value.equal(targetItem) }
        when {
            pairOptional.isPresent -> pairOptional.get().first
            else -> 0
        }
    }
    else -> 0
}


/**
 * Gets the index of any type of collection guaranteed that an
 * equal override for the class of type T is implemented.
 * <br/>
 * @see Object#equal(Object)
 *
 * @param targetItem the item to search
 * @return Optional result object
 * <br/>
 *
 * @see Optional<T> for information on how to handle return
 * @see IntPair
 */
fun <E> Collection<E>?.indexOfIntPair(targetItem: E?): Optional<IntPair<E>> = when {
    !this.isEmptyOrNull() -> Stream.of(this).findIndexed { _, value ->
        value.equal(targetItem)
     }
    else -> Optional.empty()
}


/**
 * Capitalize words for text view consumption
 */
fun String?.capitalizeWords(exceptions: List<String>) : String = when {
    !TextUtils.isEmpty(this) -> {
        val result = StringBuilder(this!!.length)
        val words = this.split("_|\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for ((index, word) in words.withIndex()) {
            when (!TextUtils.isEmpty(word)) {
                true -> if (exceptions.contains(word))
                    result.append(word)
                else
                    result.append(word.capitalize())
            }
            if (index != word.length - 1)
                result.append(" ")
        }
        result.toString()
    }
    else -> String.empty()
}

/**
 * Get a list from a given array of strings
 * @return list of capitalized strings
 */
fun  Array<String>.capitalizeWords() : List<String> =
     Stream.of(*this)
             .map { s -> s.capitalizeWords(emptyList()) }
             .toList()

fun Collection<*>?.isEmptyOrNull() : Boolean =
        this == null || this.isEmpty()

fun Collection<*>?.sizeOf() : Int = when {
    this.isEmptyOrNull() -> 0
    else -> this!!.size
}

fun <C> MutableList<C>.replaceWith(collection :Collection<C>) {
    this.clear()
    this.addAll(collection)
}

/**
 * Sorts a given map by the order of the of the keys in the map in descending order
 * @see ComparatorUtil#getKeyComparator
 */
fun <T> Map<String, T>.getKeyFilteredMap() : List<Map.Entry<String, T>> =
    Stream.of(this).sorted(ComparatorUtil.getKeyComparator()).toList()

/**
 * Checks if two objects are not null and equal
 */
fun Any?.equal(b : Any?) : Boolean =
        this != null && b != null && this == b

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
    return Math.round(this * scaledDensity)
}

fun Float.isScreenSw() : Boolean {
    val displayMetrics = Resources.getSystem().displayMetrics
    val widthDp = displayMetrics.widthPixels / displayMetrics.density
    val heightDp = displayMetrics.heightPixels / displayMetrics.density
    val screenSw = Math.min(widthDp, heightDp)
    return screenSw >= this
}

fun Float.isScreenW() : Boolean {
    val displayMetrics = Resources.getSystem().displayMetrics
    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    return screenWidth >= this
}
