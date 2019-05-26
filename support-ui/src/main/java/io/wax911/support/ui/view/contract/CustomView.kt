package io.wax911.support.ui.view.contract

/**
 * Created by max on 2017/06/24.
 * Designed to init constructors for custom views and provide coroutine contexts
 */
interface CustomView {

    val TAG
        get() = javaClass.simpleName

    /**
     * Callable in view constructors to perform view inflation and
     * additional attribute initialization
     */
    fun onInit()

    /**
     * Should be called on a view's detach from window to unbind or
     * release object references, by default this method will
     * cancel all running coroutine jobs if the current view
     * implements SupportCoroutineHelper
     *
     * @see [io.wax911.support.extension.util.SupportCoroutineHelper.cancelAllChildren]
     */
    fun onViewRecycled() {

    }
}
