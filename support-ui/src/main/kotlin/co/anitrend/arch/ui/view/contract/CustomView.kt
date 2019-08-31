package co.anitrend.arch.ui.view.contract

import android.content.Context
import android.util.AttributeSet

/**
 * Designed to init constructors for custom views and provide coroutine contexts
 *
 * @since 1.0.X
 */
interface CustomView {

    /**
     * Callable in view constructors to perform view inflation and attribute initialization
     *
     * @param context view context
     * @param attrs view attributes if applicable
     */
    fun onInit(context: Context, attrs: AttributeSet? = null)

    /**
     * Should be called on a view's detach from window to unbind or release object references
     * and cancel all running coroutine jobs if the current view
     *
     * Consider called this in [android.view.View.onDetachedFromWindow]
     */
    fun onViewRecycled() {
        // optional implementation
    }
}
