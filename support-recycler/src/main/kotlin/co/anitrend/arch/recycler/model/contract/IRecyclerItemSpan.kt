package co.anitrend.arch.recycler.model.contract

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
     *
     * @return span size to use
     */
    fun getSpanSize(
        spanCount: Int,
        position: Int
    ): Int
}