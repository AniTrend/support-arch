package io.wax911.support.util

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class SupportLifecycleUtil(context: Context?): LifecycleObserver {

    var lifecycleCallback: LifecycleCallback? = null

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
    private fun removeLifecycleObserver() = lifecycle?.removeObserver(this)

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onFragmentActivityPaused() {
        Log.d(toString(), "SupportLifecycleUtil(context: Context?)#onFragmentActivityPaused")
        lifecycleCallback?.onParentPaused()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onFragmentActivityResumed() {
        Log.d(toString(), "SupportLifecycleUtil(context: Context?)#onFragmentActivityResumed")
        lifecycleCallback?.onParentResumed()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onFragmentActivityDestroyed() {
        Log.d(toString(), "SupportLifecycleUtil(context: Context?)#onFragmentActivityDestroyed")
        lifecycleCallback?.onParentDestroyed()
        removeLifecycleObserver()
    }

    interface LifecycleCallback {

        /**
         * Called when the parent lifecycle owners state changes to
         * [androidx.fragment.app.FragmentActivity.onPause]
         *
         * @see [androidx.lifecycle.Lifecycle]
         */
        fun onParentPaused()

        /**
         * Called when the parent lifecycle owners state changes to
         * [androidx.fragment.app.FragmentActivity.onResume]
         *
         * @see [androidx.lifecycle.Lifecycle]
         */
        fun onParentResumed()

        /**
         * Called when the parent lifecycle owners state changes to
         * [androidx.fragment.app.FragmentActivity.onDestroy]
         *
         * @see [androidx.lifecycle.Lifecycle]
         */
        fun onParentDestroyed()
    }
}