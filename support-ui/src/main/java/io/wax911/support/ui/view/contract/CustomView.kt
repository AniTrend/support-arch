package io.wax911.support.ui.view.contract

import android.content.Context
import android.util.AttributeSet

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
    fun onInit(context: Context, attrs: AttributeSet? = null)

    /**
     * Should be called on a view's detach from window to unbind or
     * release object references and cancel all running coroutine jobs if the current view
     * implements [io.wax911.support.extension.util.SupportCoroutineHelper]
     */
    fun onViewRecycled()
}
