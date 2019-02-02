package io.wax911.support.view

import io.wax911.support.util.SupportCoroutineUtil
import io.wax911.support.util.SupportLifecycleUtil

/**
 * Created by max on 2017/06/24.
 * Designed to init constructors for custom views and provide coroutine contexts
 */

interface CustomView: SupportCoroutineUtil, SupportLifecycleUtil.LifecycleCallback {

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
     * @see [io.wax911.support.util.SupportCoroutineUtil.cancelAllChildren]
     */
    fun onViewRecycled() {
        cancelAllChildren()
    }
}
