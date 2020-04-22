package co.anitrend.arch.recycler.helper

import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.recycler.helper.contract.SnapPositionListener

/**
 * Helper to add snap support to [RecyclerView]
 *
 * @since v1.3.0
 */
open class SupportSnapHelper(
    private val snapPositionListener: SnapPositionListener
) : PagerSnapHelper() {

    override fun findTargetSnapPosition(
        layoutManager: RecyclerView.LayoutManager,
        velocityX: Int,
        velocityY: Int
    ): Int {
        val position = super.findTargetSnapPosition(layoutManager, velocityX, velocityY)
        if (position != RecyclerView.NO_POSITION)
            snapPositionListener.onPageChanged(position + 1)
        return position
    }
}