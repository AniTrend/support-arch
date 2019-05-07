package io.wax911.support.core.util.lifecycle.contract

interface ILifecycleCallback {

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