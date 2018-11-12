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
import java.lang.reflect.Type
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

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
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

fun Context.isConnectedToNetwork() : Boolean {
    val connectivityManager = this
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    return connectivityManager?.activeNetworkInfo?.isConnected ?: false
}

fun Context.getOkHttpCache(cacheLimit: Long) : Cache {
    val cacheDirectory = File(this.cacheDir, "response-cache")
    return Cache(cacheDirectory, cacheLimit)
}

/**
 * Returns a color from a defined attribute
 *
 * @param drawableAttr Type of attribute resource
 *
 * @return Drawable object
 */
fun Context.getDrawableFromAttr(@AttrRes drawableAttr : Int) : Drawable? {
    val drawableAttribute = this.obtainStyledAttributes(intArrayOf(drawableAttr))
    val drawable = drawableAttribute.getDrawable(0)
    drawableAttribute.recycle()
    return drawable
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

fun Int.isLightTheme() : Boolean =
        this == R.style.SupportThemeLight

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
 *
 * @param exceptions words or characters to exclude during capitalization
 */
fun String?.capitalizeWords(exceptions: List<String>) : String = when {
    !this.isNullOrEmpty() -> {
        val result = StringBuilder(this.length)
        val words = this.split("_|\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for ((index, word) in words.withIndex()) {
            when (word.isNotEmpty()) {
                true -> {
                    if (exceptions.contains(word)) result.append(word)
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
 * Get a list from a given array of strings
 *
 * @param exceptions words or characters to exclude during capitalization
 * @return list of capitalized strings
 */
fun  Array<String>.capitalizeWords(exceptions: List<String>) : List<String> =
        Stream.of(*this)
                .map { s -> s.capitalizeWords(exceptions) }
                .toList()

fun Collection<*>?.isEmptyOrNull() : Boolean =
        this?.isEmpty() == true

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

fun SupportRefreshLayout.configureWidgetBehaviorWith(context: FragmentActivity?, presenter : SupportPresenter<*>) = context?.also {
    this.setDragTriggerDistance(SupportRefreshLayout.DIRECTION_BOTTOM, (it.resources.getNavigationBarHeight()))
    this.setProgressBackgroundColorSchemeColor(it.getColorFromAttr(R.attr.rootColor))
    this.setColorSchemeColors(it.getColorFromAttr(R.attr.contentColor))
    this.setPermitRefresh(presenter.isPager)
    this.setPermitLoad(false)
    this.gone()
}

fun SupportRefreshLayout.onResponseResetStates() {
    if (this.isRefreshing) this.isRefreshing = false
    if (this.isLoading) this.isLoading = false
}

fun Context?.showContentError(progressLayout: ProgressLayout, @StringRes message: Int, @StringRes retryButtonText : Int,
                              stateLayoutOnClick: View.OnClickListener) = this?.also {
    when {
        progressLayout.isLoading || progressLayout.isEmpty || progressLayout.isContent -> {
            progressLayout.showError(this.getCompatDrawable(R.drawable.ic_support_empty_state),
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
    val intent = Intent(this, T::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    params?.also { intent.putExtras(it) }
    this?.startActivity(intent)
}

/**
 * Constructs a view model from the type defined
 */
inline fun <reified T : ViewModel> FragmentActivity.getViewModelOf() =
        ViewModelProviders.of(this).get(T::class.java)

fun SupportRecyclerView.setUpWith(supportAdapter: SupportViewAdapter<*>, vertical: Boolean, layoutManager: RecyclerView.LayoutManager? = null) {
    this.also {
        it.setHasFixedSize(true)
        it.isNestedScrollingEnabled = true
        when {
            vertical -> {
                if (layoutManager == null)
                    it.layoutManager = StaggeredGridLayoutManager(it
                            .context.resources.getInteger(R.integer.grid_list_x3),
                            StaggeredGridLayoutManager.VERTICAL)
                else
                    it.layoutManager = layoutManager
                it.adapter = supportAdapter
            }
            else -> {
                if (layoutManager == null)
                    it.layoutManager = StaggeredGridLayoutManager(it
                            .context.resources.getInteger(R.integer.single_list_size),
                            StaggeredGridLayoutManager.HORIZONTAL)
                else
                    it.layoutManager = layoutManager
                it.adapter = supportAdapter
            }
        }
    }
}
