package co.anitrend.arch.extension.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import timber.log.Timber

/**
 * Contract for life cycle aware components, with all lifecycle events optional
 *
 * @see [co.anitrend.arch.extension.ext.attachComponent]
 * @see [co.anitrend.arch.extension.ext.detachComponent]
 *
 * @since v1.2.0
 */
interface SupportLifecycle : LifecycleObserver {

    /**
     * Tag descriptor of the current module
     */
    val moduleTag: String

    /**
     * Triggered when the lifecycleOwner reaches it's onCreate state
     *
     * @see [androidx.lifecycle.LifecycleOwner]
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        Timber.tag(moduleTag).v("onCreate")
    }

    /**
     * Triggered when the lifecycleOwner reaches it's onStart state
     *
     * @see [androidx.lifecycle.LifecycleOwner]
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        Timber.tag(moduleTag).v("onStart")
    }

    /**
     * Triggered when the lifecycleOwner reaches it's onStop state
     *
     * @see [androidx.lifecycle.LifecycleOwner]
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        Timber.tag(moduleTag).v("onStop")
    }

    /**
     * Triggered when the lifecycleOwner reaches it's onResume state
     *
     * @see [androidx.lifecycle.LifecycleOwner]
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        Timber.tag(moduleTag).v("onResume")
    }

    /**
     * Triggered when the lifecycleOwner reaches it's onPause state
     *
     * @see [androidx.lifecycle.LifecycleOwner]
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        Timber.tag(moduleTag).v("onPause")
    }

    /**
     * Triggered when the lifecycleOwner reaches it's onDestroy state
     *
     * @see [androidx.lifecycle.LifecycleOwner]
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        Timber.tag(moduleTag).v("onDestroy")
    }
}