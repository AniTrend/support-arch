package io.wax911.support.util

import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import io.wax911.support.base.view.CustomView

/**
 * Created by max on 2018/08/11.
 */

class CenterSnapUtil(private val positionChangeListener: PositionChangeListener?) : PagerSnapHelper(), CustomView {

    override fun findTargetSnapPosition(layoutManager: RecyclerView.LayoutManager, velocityX: Int, velocityY: Int): Int {
        val position = super.findTargetSnapPosition(layoutManager, velocityX, velocityY)
        if (positionChangeListener != null && position != RecyclerView.NO_POSITION)
            positionChangeListener.onPageChanged(position + 1)
        return position
    }

    /**
     * Optionally included when constructing custom views
     */
    override fun onInit() {

    }

    /**
     * Clean up any resources that won't be needed
     */
    override fun onViewRecycled() {

    }

    interface PositionChangeListener {
        fun onPageChanged(currentPage: Int)
    }
}
