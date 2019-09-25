package co.anitrend.arch.extension.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import timber.log.Timber

/**
 * Contract for life cycle aware components, with all lifecycle events optional
 *
 * @since v1.2.0
 */
interface SupportLifecycle  : LifecycleObserver {

    val moduleTag: String

    /**
     * Triggered when the [androidx.lifecycle.LifecycleOwner]
     * reaches it's onStart state
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        Timber.tag(moduleTag).d("onStart")
    }

    /**
     * Triggered when the [androidx.lifecycle.LifecycleOwner]
     * reaches it's onStop state
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        Timber.tag(moduleTag).d("onStop")
    }

    /**
     * Triggered when the [androidx.lifecycle.LifecycleOwner]
     * reaches it's onResume state
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        Timber.tag(moduleTag).d("onResume")
    }

    /**
     * Triggered when the [androidx.lifecycle.LifecycleOwner]
     * reaches it's onPause state
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        Timber.tag(moduleTag).d("onPause")
    }
}