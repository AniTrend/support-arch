package io.wax911.support.ui.recycler.util

import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * @since 0.9.X
 */
class SupportSnapUtil(private val positionChangeListener: PositionChangeListener?) : PagerSnapHelper() {

    override fun findTargetSnapPosition(layoutManager: RecyclerView.LayoutManager, velocityX: Int, velocityY: Int): Int {
        val position = super.findTargetSnapPosition(layoutManager, velocityX, velocityY)
        if (positionChangeListener != null && position != RecyclerView.NO_POSITION)
            positionChangeListener.onPageChanged(position + 1)
        return position
    }

    interface PositionChangeListener {
        fun onPageChanged(currentPage: Int)
    }
}
