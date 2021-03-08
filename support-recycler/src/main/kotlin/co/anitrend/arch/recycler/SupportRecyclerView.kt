package co.anitrend.arch.recycler

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.extension.lifecycle.SupportLifecycle
import timber.log.Timber

/**
 * Extends [RecyclerView] and adds position management on stateConfiguration changes, and
 * lifecycle aware callbacks
 *
 * @see co.anitrend.arch.extension.ext.attachComponent
 * @see co.anitrend.arch.extension.ext.detachComponent
 *
 * @since v0.9.X
 */
open class SupportRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle), SupportLifecycle {

    override val moduleTag: String = javaClass.simpleName

    /**
     * Tells this recycler to set it's adapters instance to false when [SupportLifecycle.onDestroy]
     * is triggered, assuming you've called [co.anitrend.arch.extension.ext.attachComponent] on this
     */
    open var autoClearAdapter: Boolean = true

    /**
     * Indicates whether or not a scroll listener has already been added to this recycler
     */
    protected open var hasScrollListener: Boolean = false

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
     * Triggered when the lifecycleOwner reaches it's onDestroy state
     *
     * @see [androidx.lifecycle.LifecycleOwner]
     */
    override fun onDestroy() {
        super.onDestroy()
        if (autoClearAdapter) {
            Timber.tag(moduleTag).d(
                "Clearing adapter reference for this recycler to avoid potential leaks"
            )
            adapter = null
        }
    }
}
