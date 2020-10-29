package co.anitrend.arch.recycler.model.contract

import android.content.res.Resources

/**
 * Contract for recycler item with span query support
 *
 * @since v1.3.0
 */
interface IRecyclerItemSpan {

    /**
     * Provides a preferred span size for the item
     *
     * @param spanCount current span count which may also be [IRecyclerItemSpan.INVALID_SPAN_COUNT]
     * @param position position of the current item
     * @param resources optionally useful for dynamic size check with different configurations
     */
    fun getSpanSize(
        spanCount: Int,
        position: Int,
        resources: Resources
    ): Int

    companion object {
        const val INVALID_SPAN_COUNT = -1
    }
}