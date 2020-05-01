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
     * @param spanCount current span count
     * @param position position of the current item
     * @param resources potentially useful if you wish to use dynamic span
     * sizes depending on configuration based resources
     *
     * @return span size to use
     */
    fun getSpanSize(
        spanCount: Int,
        position: Int,
        resources: Resources
    ): Int
}