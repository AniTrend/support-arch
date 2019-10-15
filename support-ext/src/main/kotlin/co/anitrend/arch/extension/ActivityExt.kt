package co.anitrend.arch.extension

import android.content.Intent
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
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
 * Compares if this State is greater or equal to the given [Lifecycle.State].
 *
 * @param state State to compare with
 * @return true if this State is greater or equal to the given [Lifecycle.State]
 */
fun LifecycleOwner.isStateAtLeast(state: Lifecycle.State) =
    lifecycle.currentState.isAtLeast(state)

/**
 * Adds an observer on the lifecycle owner. This **must** be registered earlier than the lifecycle
 * events you intend on listening to, preferably [FragmentActivity.onCreate]
 *
 * @param supportLifecycle Observer notify of owner lifecycle state changes
 * @see [SupportLifecycle]
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
fun <T : Any?> FragmentActivity.extra(key: String, default: T? = null) = lazy(LAZY_MODE_PUBLICATION) {
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
fun <T : Any?> Fragment.argument(key: String, default: T? = null) = lazy(LAZY_MODE_PUBLICATION) {
    try {
        if (arguments?.containsKey(key) == true)
            arguments?.get(key) as T
        else
            default
    } catch (e: Exception) {
        error(e)
    }
}