package io.wax911.support.recycler

import android.content.Context
import android.util.AttributeSet

import androidx.recyclerview.widget.RecyclerView
import io.wax911.support.view.contract.CustomView

/**
 * Class [SupportRecyclerView] extends [RecyclerView] and adds position management on configuration changes.
 *
 * @author FrantisekGazo
 * @version 2016-03-15
 * Modified by max
 */

class SupportRecyclerView : RecyclerView, CustomView {

    private var isListenerPresent: Boolean = false

    constructor(context: Context) : super(context) {
        onInit()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        onInit()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        onInit()
    }

    /**
     * Add a listener that will be notified of any changes in scroll state or position.
     *
     *
     *
     * Components that add a listener should take care to remove it when finished.
     * Other components that take ownership of a view may call [.clearOnScrollListeners]
     * to remove all attached listeners.
     *
     * @param listener listener to set or null to clear
     */
    override fun addOnScrollListener(listener: RecyclerView.OnScrollListener) {
        super.addOnScrollListener(listener)
        isListenerPresent = true
    }

    /**
     * Remove all secondary listener that were notified of any changes in scroll state or position.
     */
    override fun clearOnScrollListeners() {
        super.clearOnScrollListeners()
        isListenerPresent = false
    }

    /*To avoid multiple instances of scroll listener from being added*/
    fun hasOnScrollListener() = isListenerPresent


    /**
     * Optionally included when constructing custom views
     */
    override fun onInit() {

    }
}
