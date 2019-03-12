package io.wax911.support

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Bundle
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.annimon.stream.IntPair
import com.annimon.stream.Objects
import com.annimon.stream.Optional
import com.annimon.stream.Stream
import com.nguyenhoanglam.progresslayout.ProgressLayout
import io.wax911.support.custom.presenter.SupportPresenter
import io.wax911.support.custom.recycler.SupportRecyclerView
import io.wax911.support.custom.recycler.SupportViewAdapter
import io.wax911.support.custom.widget.SupportRefreshLayout
import okhttp3.Cache
import java.io.File
import java.util.*

object ComparatorUtil {
    fun <T> getKeyComparator(): Comparator<Map.Entry<String, T>> =
            kotlin.Comparator { o1, o2 -> o1.key.compareTo(o2.key)  }
}

/**
 * Toggles between light and dark theme
 *
 * @return Style resource
 */
@StyleRes
fun Int.swapTheme() : Int = when (this == R.style.SupportThemeLight) {
    true -> R.style.SupportThemeDark
    false -> R.style.SupportThemeLight
}

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
 * Potentially useless but returns an empty string, the signature may change in future
 *
 * @see String.isNullOrBlank
 */
fun String.Companion.empty() = ""

/**
 * Request to hide the soft input window from the context of the window
 * that is currently accepting input. This should be called as a result
 * of the user doing some actually than fairly explicitly requests to
 * have the input window hidden.
 */
fun FragmentActivity?.hideKeyboard() = this?.apply {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(window.decorView.windowToken, 0)
}

/**
 * Exactly whether a device is low-RAM is ultimately up to the device configuration, but currently
 * it generally means something in the class of a 512MB device with about a 800x480 or less screen.
 * This is mostly intended to be used by apps to determine whether they should
 * turn off certain features that require more RAM.
 *
 * @return true if this is a low-RAM device.
 */
fun Context?.isLowRamDevice() : Boolean = this?.let {
    val activityManager = it.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    return ActivityManagerCompat.isLowRamDevice(activityManager)
} ?: false

/**
 * Check if the device has any active network connections like WiFi or Network data,
 * preferably use broadcast receivers if you want to do live updates of the internet connectivity status
 *
 * @return true if network connectivity exists, false otherwise.
 */
fun Context?.isConnectedToNetwork() : Boolean = this?.let {
    val connectivityManager = it
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    return connectivityManager?.activeNetworkInfo?.isConnected ?: false
} ?: false

/**
 * Creates an abstract file that will be used by OkHttp to cache HTTP and HTTPS responses
 * to the filesystem so they may be reused, saving time and bandwidth.
 *
 * @return Cache object
 */
fun Context?.getOkHttpCache(cacheLimit: Long) : Cache? = this?.let {
    val cacheDirectory = File(it.cacheDir, "response-cache")
    return Cache(cacheDirectory, cacheLimit)
}

/**
 * Creates a drawable from the given attribute resource which cannot be nullable type
 *
 * @param drawableAttr attribute resource for drawable
 * @return Drawable for the attribute, or null if not defined.
 * @throws UnsupportedOperationException if the attribute is defined but is
 *         not a color or drawable resource.
 */
fun Context.getDrawableFromAttr(@AttrRes drawableAttr : Int) : Drawable? {
    val drawableAttribute = obtainStyledAttributes(intArrayOf(drawableAttr))
    val drawable = drawableAttribute.getDrawable(0)
    drawableAttribute.recycle()
    return drawable
}

/**
 * Creates a color from the given attribute, If the attribute references a color resource holding a complex
 * @link{android.content.res.ColorStateList}, then the default color from the set is returned.
 *
 * @param colorAttr attribute resource for color
 * @return Attribute color value, or defValue if not defined.
 * @throws UnsupportedOperationException if the attribute is defined but is
 *         not a color or drawable resource.
 */
fun Context.getColorFromAttr(@AttrRes colorAttr : Int, defaultColor : Int = 0) : Int {
    val colorAttribute = obtainStyledAttributes(intArrayOf(colorAttr))
    @ColorInt val color = colorAttribute.getColor(0, defaultColor)
    colorAttribute.recycle()
    return color
}

/**
 * Starting in android Marshmallow, the returned
 * color will be styled for the specified Context's theme.
 *
 * @see android.os.Build.VERSION_CODES.M
 * @return A single color value in the form 0xAARRGGBB.
 */
fun Context.getCompatColor(@ColorRes colorRes: Int) =
        ContextCompat.getColor(this, colorRes)

/**
 * Avoids resource not found when using vector drawables in API levels < Lollipop
 *
 * This method supports inflation of {@code <vector>}, {@code <animated-vector>} and
 * {@code <animated-selector>} resources on devices where platform support is not available.
 *
 * @param resource The resource id of the drawable or vector drawable
 *                 @see DrawableRes
 *
 * @return Drawable An object that can be used to draw this resource.
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
 * @param tintColor A specific color to tint the drawable
 * @return Drawable tinted with the tint color
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
 * of each drawable is not shared.
 *
 * @return Drawable tinted with {@link R.attr.titleColor}
 * @param resource The resource id of the drawable or vector drawable
 * @return Drawable tinted with the tint color
 */
fun Context.getTintedDrawable(@DrawableRes resource : Int) : Drawable? {
    val originalDrawable = getCompatDrawable(resource)
    var drawable : Drawable? = null
    if (originalDrawable != null) {
        drawable = DrawableCompat.wrap(originalDrawable).mutate()
        DrawableCompat.setTint(drawable, getColorFromAttr(R.attr.titleColor))
    }
    return drawable
}

/**
 * Avoids resource not found when using vector drawables in API levels < Lollipop
 * and tints the drawable depending on the current selected theme, images loaded
 * from this method apply the {@link Drawable#mutate()} to assure that the state
 * of each drawable is not shared
 *
 * @param resource The resource id of the drawable or vector drawable
 * @param colorAttr A specific color to tint the drawable
 * @return Drawable tinted with the tint color
 */
fun Context.getTintedDrawable(@DrawableRes resource : Int, @AttrRes colorAttr : Int) : Drawable? {
    val originalDrawable = getCompatDrawable(resource)
    var drawable : Drawable? = null
    if (originalDrawable != null) {
        drawable = DrawableCompat.wrap(originalDrawable).mutate()
        DrawableCompat.setTint(drawable, getColorFromAttr(colorAttr))
    }
    return drawable
}

/**
 * Gets the size of the display, in pixels. Value returned by this method does
 * not necessarily represent the actual raw size (native resolution) of the display.
 *
 * @return A Point object to with the size information.
 * @see Point
 */
fun Context.getScreenDimens() : Point {
    val deviceDimens = Point()
    (getSystemService(Context.WINDOW_SERVICE) as WindowManager).apply {
        defaultDisplay?.getSize(deviceDimens)
    }
    return deviceDimens
}

/**
 * Starts a shared transition of activities connected by views
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

fun Int.isLightTheme() : Boolean =
        this == R.style.SupportThemeLight

fun Context.getLayoutInflater() : LayoutInflater =
        this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

/**
 * Credits
 * @author hamakn
 * https://gist.github.com/hamakn/8939eb68a920a6d7a498
 */
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
 */
fun Resources.getNavigationBarHeight() : Int {
    var navigationBarHeight = 0
    val resourceId = this.getIdentifier("navigation_bar_height", "dimen", "android")
    if (resourceId > 0)
        navigationBarHeight = this.getDimensionPixelSize(resourceId)
    return navigationBarHeight
}

/**
 * Credits
 * @author hamakn
 * https://gist.github.com/hamakn/8939eb68a920a6d7a498
 */
fun FragmentActivity.getActionBarHeight() : Int {
    val styledAttributes = this.theme.obtainStyledAttributes(
            intArrayOf(android.R.attr.actionBarSize)
    )
    styledAttributes.recycle()
    return styledAttributes.getDimension(0, 0f).toInt()
}

/**
 * Creates a list of the array resource given
 *
 * @return The string list associated with the resource.
 * @throws Exception if the given ID does not exist.
 */
fun Context.getStringList(@ArrayRes arrayRes : Int) : List<String> {
    val array = resources.getStringArray(arrayRes)
    return array.toList()
}

/**
 * Gets the index of any type of iterable guaranteed that an equal override for the class
 * of type T is implemented.
 *
 * @param targetItem the item to search
 * @return 0 if no result was found
 */
fun <A> Iterable<A>?.indexOfIterable(targetItem: A?): Int = when (this != null) {
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
    !this.isNullOrEmpty() -> Stream.of(this).findIndexed { _, value ->
        value.equal(targetItem)
    }
    else -> Optional.empty()
}

/**
 * Returns a copy of this strings having its first letter uppercase, or the original string,
 * if it's empty or already starts with an upper case letter.
 *
 * @param exceptions words or characters to exclude during capitalization
 */
fun String?.capitalizeWords(exceptions: List<String>? = null) : String = when {
    !this.isNullOrEmpty() -> {
        val result = StringBuilder(this.length)
        val words = this.split("_|\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for ((index, word) in words.withIndex()) {
            when (word.isNotEmpty()) {
                true -> {
                    if (!exceptions.isNullOrEmpty() && exceptions.contains(word)) result.append(word)
                    else result.append(word.capitalize())
                }
            }
            if (index != words.size - 1)
                result.append(" ")
        }
        result.toString()
    }
    else -> String.empty()
}

/**
 * Capitalizes all the strings unless they are specified in the exceptions list
 *
 * @param exceptions words or characters to exclude during capitalization
 * @return list of capitalized strings
 */
fun  Array<String>.capitalizeWords(exceptions: List<String>? = null) = Stream.of(*this)
        .map { s -> s.capitalizeWords(exceptions) }
        .toList()

/**
 * Gives the size of the collection by doing a null check first
 *
 * @return 0 if the collection is null or empty otherwise the size of the collection
 */
fun Collection<*>?.sizeOf() : Int = when {
    this.isNullOrEmpty() -> 0
    else -> this.size
}

/**
 * Clears the current list and adds the new items into the collection replacing all items
 */
fun <C> MutableList<C>.replaceWith(collection :Collection<C>) {
    clear(); addAll(collection)
}

/**
 * Sorts a given map by the order of the of the keys in the map in descending order
 *
 * @see ComparatorUtil.getKeyComparator
 */
fun <T> Map<String, T>.getKeyFilteredMap() : List<Map.Entry<String, T>> =
        Stream.of(this).sorted(ComparatorUtil.getKeyComparator()).toList()

/**
 * @return true if two objects are the same otherwise false if one of them is null or both are not equal
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
 * This method applies the most common configuration for the widget, things like direction, colors, behavior etc.
 */
fun SupportRefreshLayout.configureWidgetBehaviorWith(context: FragmentActivity?, presenter : SupportPresenter<*>) = context?.also {
    setDragTriggerDistance(SupportRefreshLayout.DIRECTION_BOTTOM, (it.resources.getNavigationBarHeight()))
    setProgressBackgroundColorSchemeColor(it.getColorFromAttr(R.attr.rootColor))
    setColorSchemeColors(it.getColorFromAttr(R.attr.contentColor))
    setPermitRefresh(presenter.isPager)
    setPermitLoad(false)
    gone()
}

/**
 * Resets the refreshing or loading states when called, common use case would be after a network response
 */
fun SupportRefreshLayout.onResponseResetStates() {
    if (isRefreshing) isRefreshing = false
    if (isLoading) isLoading = false
}


fun Context?.showContentError(progressLayout: ProgressLayout, @StringRes message: Int, @StringRes retryButtonText : Int,
                              stateLayoutOnClick: View.OnClickListener) = this?.also {
    when {
        progressLayout.isLoading || progressLayout.isEmpty || progressLayout.isContent -> {
            progressLayout.showError(getCompatDrawable(R.drawable.ic_support_empty_state),
                    it.getString(message), it.getString(retryButtonText), stateLayoutOnClick)
        }
        else -> {
        }
    }
}

fun ProgressLayout.showContentLoading() = when {
    this.isContent || this.isEmpty ||
            this.isError -> this.showLoading()
    else -> { }
}

fun ProgressLayout.showLoadedContent() = when {
    this.isLoading || this.isEmpty ||
            this.isError -> this.showContent()
    else -> { }
}

/**
 * Start a new activity from context and avoid potential crashes from early API levels
 */
inline fun <reified T> Context?.startNewActivity(params: Bundle?) {
    try {
        val intent = Intent(this, T::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        params?.also { intent.putExtras(it) }
        this?.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

inline fun <reified T : ViewModel> FragmentActivity.getViewModelOf() =
        ViewModelProviders.of(this).get(T::class.java)


/**
 * Sets up a recycler view by handling all the boilerplate code associated with it using
 * the given layout manager or the default.
 *
 * @param supportAdapter recycler view adapter which will be used
 * @param vertical if the layout adapter should be vertical or horizontal
 * @param layoutManager optional layout manager if you do not wish to use the default
 */
fun SupportRecyclerView.setUpWith(supportAdapter: SupportViewAdapter<*>, vertical: Boolean = true,
                                  layoutManager: RecyclerView.LayoutManager? = null) {
    setHasFixedSize(true)
    isNestedScrollingEnabled = true
    when {
        vertical -> {
            if (layoutManager == null)
                this.layoutManager = StaggeredGridLayoutManager(
                        context.resources.getInteger(R.integer.grid_list_x3),
                        StaggeredGridLayoutManager.VERTICAL)
            else
                this.layoutManager = layoutManager
            adapter = supportAdapter
        }
        else -> {
            if (layoutManager == null)
                this.layoutManager = StaggeredGridLayoutManager(
                        context.resources.getInteger(R.integer.single_list_size),
                        StaggeredGridLayoutManager.HORIZONTAL)
            else
                this.layoutManager = layoutManager
            adapter = supportAdapter
        }
    }
}