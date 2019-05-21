package io.wax911.support.core.view.contract

import io.wax911.support.core.util.SupportCoroutineUtil
import io.wax911.support.core.util.lifecycle.SupportLifecycleUtil
import io.wax911.support.core.util.lifecycle.contract.ILifecycleCallback

/**
 * Created by max on 2017/06/24.
 * Designed to init constructors for custom views and provide coroutine contexts
 */
interface CustomView: SupportCoroutineUtil, ILifecycleCallback {

    /**
     * Callable in view constructors to perform view inflation and
     * additional attribute initialization
     */
    fun onInit()

    /**
     * Should be called on a view's detach from window to unbind or
     * release object references, by default this method will
     * cancel all running coroutine jobs
     *
     * @see [io.wax911.support.core.util.SupportCoroutineUtil.cancelAllChildren]
     */
    fun onViewRecycled() {
        cancelAllChildren()
    }

    /**
     * Called when the parent lifecycle owners state changes to
     * [androidx.fragment.app.FragmentActivity.onPause]
     *
     * @see [androidx.lifecycle.Lifecycle]
     */
    override fun onParentPaused() { }

    /**
     * Called when the parent lifecycle owners state changes to
     * [androidx.fragment.app.FragmentActivity.onResume]
     *
     * @see [androidx.lifecycle.Lifecycle]
     */
    override fun onParentResumed() { }

    /**
     * Called when the parent lifecycle owners state changes to
     * [androidx.fragment.app.FragmentActivity.onDestroy]
     *
     * @see [androidx.lifecycle.Lifecycle]
     */
    override fun onParentDestroyed() { }
}
