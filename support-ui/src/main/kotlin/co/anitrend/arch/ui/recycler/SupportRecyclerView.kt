package co.anitrend.arch.ui.recycler

import android.content.Context
import android.util.AttributeSet

import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.ui.view.contract.CustomView

/**
 * Extends [RecyclerView] and adds position management on stateConfiguration changes.
 *
 * @author wax911
 * @since 0.9.X
 */
class SupportRecyclerView : RecyclerView, CustomView {

    /**
     * Indicates whether or not a scroll listener has already been added to this recycler
     */
    var hasScrollListener: Boolean = false
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
        if (!hasScrollListener) {
            super.addOnScrollListener(listener)
            hasScrollListener = true
        }
    }

    /**
     * Remove all secondary listener that were notified of any changes in scroll state or position.
     */
    override fun clearOnScrollListeners() {
        super.clearOnScrollListeners()
        hasScrollListener = false
    }

    /**
     * Callable in view constructors to perform view inflation and attribute initialization
     *
     * @param context view context
     * @param attrs view attributes if applicable
     */
    override fun onInit(context: Context, attrs: AttributeSet?) {

    }
}
