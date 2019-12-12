package co.anitrend.arch.ui.recycler

import androidx.recyclerview.widget.RecyclerView

/**
 * Default implementation of an observer proxy for relying recycler adapter updates
 *
 * @param adapterDataObserver the observer from [RecyclerView.Adapter.registerAdapterDataObserver]
 * @param additionalViewAdapterViewCount number of additional views that could be rendered
 *
 * @since v1.3.0
 * @see RecyclerView.AdapterDataObserver
 * @see RecyclerView.Adapter.registerAdapterDataObserver
 */
class SupportAdapterObserverProxy(
    private val adapterDataObserver: RecyclerView.AdapterDataObserver,
    private val additionalViewAdapterViewCount: Int
) : RecyclerView.AdapterDataObserver() {

    override fun onChanged() {
        adapterDataObserver.onChanged()
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        adapterDataObserver.onItemRangeRemoved(
            positionStart + additionalViewAdapterViewCount,
            itemCount
        )
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        adapterDataObserver.onItemRangeMoved(
            fromPosition + additionalViewAdapterViewCount,
            toPosition,
            itemCount
        )
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        adapterDataObserver.onItemRangeInserted(
            positionStart + additionalViewAdapterViewCount,
            itemCount
        )
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        adapterDataObserver.onItemRangeChanged(
            positionStart + additionalViewAdapterViewCount,
            itemCount
        )
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        adapterDataObserver.onItemRangeChanged(
            positionStart + additionalViewAdapterViewCount,
            itemCount,
            payload
        )
    }
}