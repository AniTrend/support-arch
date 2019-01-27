package io.wax911.support.util

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class SupportLifecycleUtil(context: Context): LifecycleObserver {

    var lifecycleCallback: LifecycleCallback? = null

    init {
        if (context is FragmentActivity)
            context.lifecycle.addObserver(this)
    }

    /**
     * Unregister the current class to the parent lifecycle owner allowing it to respond to lifecycle
     * changes.
     *
     * @param lifecycle owner that has the lifecycle
     */
    fun removeLifecycleObserver(lifecycle: Lifecycle?) {
        lifecycle?.removeObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onFragmentActivityPaused() {
        Log.d(toString(), "onFragmentActivityPaused")
        lifecycleCallback?.onParentPaused()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onFragmentActivityDestroyed() {
        Log.d(toString(), "onFragmentActivityDestroyed")
        this.lifecycleCallback = null
    }

    interface LifecycleCallback {

        /**
         * called when the parent lifecycle owners state changes to onPause()
         * @see Lifecycle
         */
        fun onParentPaused()
    }
}