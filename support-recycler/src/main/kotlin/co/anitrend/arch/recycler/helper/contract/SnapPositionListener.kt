package co.anitrend.arch.recycler.helper.contract

/**
 * Position change listener for snapping items
 *
 * @since v1.3.0
 */
interface SnapPositionListener {
    fun onPageChanged(currentPage: Int)
}