package io.wax911.support.core.util.lifecycle

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.wax911.support.core.util.lifecycle.contract.ILifecycleCallback
import timber.log.Timber

/**
 * Lifecycle observer helper for modules without a lifecycle instance
 */
class SupportLifecycleUtil(
    context: Context?
): LifecycleObserver {

    var lifecycleCallback: ILifecycleCallback? = null

    private val lifecycle by lazy {
        context?.let {
            if (it is FragmentActivity)
                return@lazy it.lifecycle
            else
                null
        }
    }

    init {
        lifecycle?.addObserver(this)
    }

    /**
     * Unregister the current class to the parent lifecycle owner allowing it to respond to lifecycle
     * changes.
     */
    private fun removeLifecycleObserver() {
        lifecycle?.removeObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onFragmentActivityPaused() {
        Timber.tag(toString()).d("SupportLifecycleUtil(context: Context?)#onFragmentActivityPaused")
        lifecycleCallback?.onParentPaused()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onFragmentActivityResumed() {
        Timber.tag(toString()).d("SupportLifecycleUtil(context: Context?)#onFragmentActivityResumed")
        lifecycleCallback?.onParentResumed()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onFragmentActivityDestroyed() {
        Timber.tag(toString()).d("SupportLifecycleUtil(context: Context?)#onFragmentActivityDestroyed")
        lifecycleCallback?.onParentDestroyed()
        removeLifecycleObserver()
    }
}