package co.anitrend.arch.extension.ext

import android.app.ActivityManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.*
import android.content.res.TypedArray
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.FragmentActivity
import timber.log.Timber
import java.util.*
import kotlin.system.exitProcess

/**
 * Extension for getting system services
 */
inline fun <reified T> Context.systemServiceOf(): T? =
    ContextCompat.getSystemService(this, T::class.java)

/**
 * Starts a foreground service using the specified type and action
 *
 * @see stopServiceMatching
 */
inline fun <reified T> Context.startServiceInForeground(intentAction: String) {
    val intent = Intent(this, T::class.java).apply {
        action = intentAction
        setClass(this@startServiceInForeground, T::class.java)
    }
    ContextCompat.startForegroundService(this, intent)
}

/**
 * Starts a background service using the specified type and action
 *
 * @see stopServiceMatching
 */
inline fun <reified T> Context.startServiceInBackground(intentAction: String): ComponentName? {
    val intent = Intent(this, T::class.java).apply {
        action = intentAction
        setClass(this@startServiceInBackground, T::class.java)
    }
    return startService(intent)
}

/**
 * Request that a given application service be stopped. If the service is
 * not running, nothing happens.
 *
 * @return If there is a service matching the given Intent that is already
 * running, then it is stopped and `true` is returned; else `false` is returned.
 *
 * @see Context.stopService
 */
inline fun <reified T> Context.stopServiceMatching(intentAction: String): Boolean {
    val intent = Intent(this, T::class.java).apply {
        action = intentAction
        setClass(this@stopServiceMatching, T::class.java)
    }
    return stopService(intent)
}

/**
 * Extension helper for context that helps us restart the application
 *
 * @param intentId Id of the pending intent which will be used
 */
inline fun <reified T> Context.restartApplication(intentId: Int = 1510, delayDuration: Int = 100) {
    runCatching {
        val startTargetIntent = Intent(this, T::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, intentId, startTargetIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val alarmManager = systemServiceOf<AlarmManager>()
        alarmManager?.set(
            AlarmManager.RTC,
            System.currentTimeMillis() + delayDuration,
            pendingIntent
        )
        exitProcess(0)
    }.onFailure {
        Timber.tag("restartApplication").e(it)
    }
}

/**
 * Schedule a repeating alarm that has inexact trigger time requirements.
 *
 * @param enabled schedules or cancels the scheduled task
 * @param interval duration between each alarm event
 */
inline fun <reified T> Context.scheduleWithAlarm(enabled: Boolean, interval: Long) {
    val intent = Intent(this, T::class.java)
    val pendingIntent: PendingIntent? = PendingIntent.getBroadcast(
        this,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    val alarmManager = systemServiceOf<AlarmManager>()
    alarmManager?.run {
        if (!enabled) {
            cancel(pendingIntent)
        } else {
            cancel(pendingIntent)
            if (interval > 0)
                setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    Calendar.getInstance().timeInMillis,
                    interval,
                    pendingIntent
                )
        }
    }
}

/**
 * Sets the given [content] into clipboard.
 */
fun Context.copyToClipboard(label: String, content: String) {
    val clipboardManager = systemServiceOf<ClipboardManager>()
    val clip = ClipData.newPlainText(label, content)
    clipboardManager?.setPrimaryClip(clip)
}

/**
* Request to show or hide the soft input window from the context of the window that is currently
 * accepting input.
 *
 * This should be called as a result of the user doing some actually than fairly explicitly
 * requests to have the input window shown or hidden.
 *
 * @param show True if the keyboard should be shown otherwise False to hide it
*/
@Deprecated(
    message = "Use View.toggleKeyboard(show) extension instead",
    level = DeprecationLevel.WARNING
)
fun Context.toggleKeyboard(show: Boolean) {
    runCatching {
        val windowToken = (this as FragmentActivity).window.decorView.windowToken
        val inputMethodManager = systemServiceOf<InputMethodManager>()
        if (inputMethodManager != null && windowToken != null)
            if (show)
                inputMethodManager.toggleSoftInput(
                    InputMethodManager.SHOW_FORCED,
                    0
                )
            else
                inputMethodManager.hideSoftInputFromWindow(
                    windowToken, 0
                )
    }.onFailure {
        Timber.tag("toggleKeyboard").e(it)
    }
}

/**
 * Exactly whether a device is low-RAM is ultimately up to the device configuration, but currently
 * it generally means something in the class of a 512MB device with about a 800x480 or less screen.
 * This is mostly intended to be used by apps to determine whether they should
 * turn off certain features that require more RAM.
 *
 * @return true if this is a low-RAM device.
 */
fun Context?.isLowRamDevice(): Boolean {
    val activityManager = this?.systemServiceOf<ActivityManager>()
    return if (activityManager != null)
         ActivityManagerCompat.isLowRamDevice(activityManager)
    else false
}

/**
 * Start a new activity from context and avoid potential crashes from early API levels
 *
 * @param params Bundle of extras to add to the target activity
 * @param options Additional options for how the Activity should be started from
 */
inline fun <reified T> Context?.startNewActivity(params: Bundle? = null, options: Bundle? = null) {
    runCatching {
        val intent = Intent(this, T::class.java)
        with (intent) {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            params?.also { putExtras(it) }
        }
        this?.startActivity(intent, options)
    }.onFailure {
        Timber.tag("startNewActivity").e(it)
    }
}

/**
 * Creates a list of the array resource given
 *
 * @return The string list associated with the resource.
 *
 * @throws Exception if the given ID does not exist.
 */
fun Context.getStringList(@ArrayRes arrayRes : Int): List<String> {
    val array = resources.getStringArray(arrayRes)
    return array.toList()
}

fun View.getLayoutInflater(): LayoutInflater =
    context.getLayoutInflater()

fun Context.getLayoutInflater(): LayoutInflater =
    systemServiceOf<LayoutInflater>()!!

/**
 * Gets the size of the display, in pixels. Value returned by this method does
 * not necessarily represent the actual raw size (native resolution) of the display.
 *
 * @return A Point object to with the size information.
 *
 * @see Point
 */
fun Context.getScreenDimens(): Point {
    val deviceDimens = Point()
    systemServiceOf<WindowManager>()
        ?.defaultDisplay
        ?.getSize(deviceDimens)
    return deviceDimens
}

/**
 * Creates a drawable from the given attribute resource which cannot be nullable type
 *
 * @param drawableAttr attribute resource for drawable
 *
 * @return Drawable for the attribute, or null if not defined.
 *
 * @throws UnsupportedOperationException if the attribute is defined but is
 *         not a color or drawable resource.
 */
fun Context.getDrawableAttr(@AttrRes drawableAttr : Int): Drawable? {
    val drawableAttribute = obtainStyledAttributes(intArrayOf(drawableAttr))
    val drawable = drawableAttribute.getDrawable(0)
    drawableAttribute.recycle()
    return drawable
}

/**
 * Creates a color from the given attribute, If the attribute references a color resource holding a complex
 * [android.content.res.ColorStateList], then the default color from the set is returned.
 *
 * @param colorAttr attribute resource for color
 *
 * @return Attribute color value, or defValue if not defined.
 *
 * @throws UnsupportedOperationException if the attribute is defined but is
 *         not a color or drawable resource.
 */
fun Context.getColorFromAttr(@AttrRes colorAttr : Int, defaultColor : Int = 0): Int {
    val colorAttribute = obtainStyledAttributes(intArrayOf(colorAttr))
    @ColorInt val color = colorAttribute.getColor(0, defaultColor)
    colorAttribute.recycle()
    return color
}

/**
 * Starting in android Marshmallow, the returned
 * color will be styled for the specified Context's theme.
 *
 * @return A single color value in the form 0xAARRGGBB.
 *
 * @see android.os.Build.VERSION_CODES.M
 */
fun Context.getCompatColor(@ColorRes colorRes: Int) =
    ContextCompat.getColor(this, colorRes)

/**
 * Avoids resource not found when using vector drawables in API levels < Lollipop
 *
 * This method supports inflation of `<vector>`, `<animated-vector>` and
 * `<animated-selector>` resources on devices where platform support is not available.
 *
 * @param resource The resource id of the drawable or vector drawable
 *
 * @return [Drawable] object that can be used to draw this resource.
 *
 * @see Drawable
 * @see DrawableRes
 */
fun Context.getCompatDrawable(@DrawableRes resource : Int) =
    AppCompatResources.getDrawable(this, resource)

/**
 * Avoids resource not found when using vector drawables in API levels < Lollipop
 * Also images loaded from this method apply the [Drawable.mutate] to assure
 * that the state of each drawable is not shared
 *
 * @param resource The resource id of the drawable or vector drawable
 * @param tintColor A specific color to tint the drawable
 *
 * @return [Drawable] tinted with the tint color
 *
 * @see Drawable
 * @see DrawableRes
 */
fun Context.getCompatDrawable(@DrawableRes resource : Int, @ColorRes tintColor : Int): Drawable? {
    val drawableResource = AppCompatResources.getDrawable(this, resource)
    if (drawableResource != null) {
        val drawableResult = DrawableCompat.wrap(drawableResource).mutate()
        if (tintColor != 0)
            DrawableCompat.setTint(drawableResult, getCompatColor(tintColor))
        return drawableResource
    }
    return null
}

/**
 * Avoids resource not found when using vector drawables in API levels < Lollipop
 * and tints the drawable depending on the current selected theme, images loaded
 * from this method apply the [Drawable.mutate] to assure that the state
 * of each drawable is not shared.
 *
 * @param resource The resource id of the drawable or vector drawable
 *
 * @return [Drawable] tinted with [colorAttr]
 *
 * @see AttrRes
 * @see Drawable
 * @see DrawableRes
 */
fun Context.getTintedDrawableWithAttribute(
    @DrawableRes resource : Int,
    @AttrRes colorAttr: Int
): Drawable? {
    val originalDrawable = getCompatDrawable(resource)
    var drawable : Drawable? = null
    if (originalDrawable != null) {
        drawable = DrawableCompat.wrap(originalDrawable).mutate()
        DrawableCompat.setTint(drawable, getColorFromAttr(colorAttr))
    }
    return drawable
}

/**
 * Avoids resource not found when using vector drawables in API levels < Lollipop
 * and tints the drawable depending on the current selected theme, images loaded
 * from this method apply the [Drawable.mutate] to assure that the state
 * of each drawable is not shared
 *
 * @param resource The resource id of the drawable or vector drawable
 * @param colorAttr A specific color to tint the drawable
 *
 * @return [Drawable] tinted with the tint color
 *
 * @see AttrRes
 * @see Drawable
 * @see DrawableRes
 */
fun Context.getTintedDrawable(@DrawableRes resource : Int, @AttrRes colorAttr : Int): Drawable? {
    val originalDrawable = getCompatDrawable(resource)
    var drawable : Drawable? = null
    if (originalDrawable != null) {
        drawable = DrawableCompat.wrap(originalDrawable).mutate()
        DrawableCompat.setTint(drawable, getColorFromAttr(colorAttr))
    }
    return drawable
}

/**
 * Auto disposable extension for recycling and catching exceptions for typed arrays
 */
inline fun TypedArray.use(block: (TypedArray) -> Unit) {
    runCatching {
        block(this)
        recycle()
    }.onFailure {
        Timber.tag("TypedArray.use").e(it)
    }
}