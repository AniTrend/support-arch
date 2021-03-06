package co.anitrend.arch.extension.ext

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import co.anitrend.arch.extension.lifecycle.SupportLifecycle

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

/**
 * Disables toolbar title for an activity
 */
fun FragmentActivity.disableToolbarTitle() {
    actionBar?.setDisplayShowTitleEnabled(false)
}

/**
 * Makes status bar transparent
 */
fun FragmentActivity.makeStatusBarTransparent() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            statusBarColor = Color.TRANSPARENT
        }
    }
}

/**
 * Hides both status bar and navigation bar
 */
fun FragmentActivity.hideStatusBarAndNavigationBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        val controller = ViewCompat.getWindowInsetsController(window.decorView)

        // swipe in system bars, this is now sticky immersive
        controller?.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // hide status and navigation bar
        controller?.hide(WindowInsets.Type.systemBars())
    } else {
        // Until I figure out how to hide status bar and nav pre android R using insets
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
}

/**
 * Adds an observer on the lifecycle owner. This **must** be registered earlier than the lifecycle
 * events you intend on listening to, preferably [FragmentActivity.onCreate]
 *
 * @param supportLifecycle Observer notify of owner lifecycle state changes
 */
fun LifecycleOwner.attachComponent(supportLifecycle: SupportLifecycle) =
    lifecycle.addObserver(supportLifecycle)

/**
 * Removes an observer on the lifecycle owner. This **must** be un-registered later than the lifecycle
 * events you being listened to, preferably [FragmentActivity.onDestroy]
 *
 * @param supportLifecycle Observer notify of owner lifecycle state changes
 */
fun LifecycleOwner.detachComponent(supportLifecycle: SupportLifecycle) =
    lifecycle.removeObserver(supportLifecycle)

/**
 * Lazy intent parameters for fragment activities
 *
 * @param key lookup key for the embedded item in the [FragmentActivity.getIntent]
 * @param default default value to use when key does not exist
 *
 * @return [Lazy] of the target type
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any?> FragmentActivity.extra(key: String, default: T? = null) = lazy(PUBLICATION) {
    try {
        if (intent?.extras?.containsKey(key) == true)
            intent?.extras?.get(key) as T
        else
            default
    } catch (e: Exception) {
        error(e)
    }
}

/**
 * Lazy intent parameters for fragments
 *
 * @param key lookup key for the embedded item in the [Fragment.getArguments]
 * @param default default value to use when key does not exist
 *
 * @return [Lazy] of the target type
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any?> Fragment.argument(key: String, default: T? = null) = lazy(PUBLICATION) {
    try {
        if (arguments?.containsKey(key) == true)
            arguments?.get(key) as T
        else
            default
    } catch (e: Exception) {
        error(e)
    }
}