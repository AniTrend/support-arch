package io.wax911.support.ui.recycler

import android.content.Context
import android.util.AttributeSet

import androidx.recyclerview.widget.RecyclerView
import io.wax911.support.ui.view.contract.CustomView

/**
 * Class [SupportRecyclerView] extends [RecyclerView] and adds position management on configuration changes.
 *
 * @author FrantisekGazo
 * @author wax911
 * @since 0.9.X
 */
class SupportRecyclerView : RecyclerView, CustomView {

    /**
     * Helps avoid multiple instances of scroll listener from being added
     */
    var isScrollListenerSet: Boolean = false
        private set

    constructor(context: Context) :
            super(context) { onInit(context) }
    constructor(context: Context, attrs: AttributeSet?) :
            super(context, attrs) { onInit(context, attrs) }
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) :
            super(context, attrs, defStyle) { onInit(context, attrs) }

    /**
     * Add a listener that will be notified of any changes in scroll state or position.
     *
     * Components that add a listener should take care to remove it when finished.
     * Other components that take ownership of a view may call [.clearOnScrollListeners]
     * to remove all attached listeners.
     *
     * @param listener listener to set or null to clear
     */
    override fun addOnScrollListener(listener: OnScrollListener) {
        if (!isScrollListenerSet) {
            super.addOnScrollListener(listener)
            isScrollListenerSet = true
        }
    }

    /**
     * Remove all secondary listener that were notified of any changes in scroll state or position.
     */
    override fun clearOnScrollListeners() {
        super.clearOnScrollListeners()
        isScrollListenerSet = false
    }

    /**
     * Optionally included when constructing custom views
     */
    override fun onInit(context: Context, attrs: AttributeSet?) {

    }

    /**
     * Should be called on a view's detach from window to unbind or
     * release object references and cancel all running coroutine jobs if the current view
     */
    override fun onViewRecycled() {

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onViewRecycled()
    }
}
