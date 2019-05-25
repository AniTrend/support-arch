package io.wax911.support.ui.view.image

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec.*
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import io.wax911.support.core.view.contract.CustomView
import io.wax911.support.extension.getScreenDimens
import io.wax911.support.ui.R

/**
 * Portrait image view with a 4:3 aspect ratio
 */
class SupportPortraitImageView : AppCompatImageView, CustomView {

    constructor(context: Context?) :
            super(context) { onInit() }
    constructor(context: Context?, attrs: AttributeSet?) :
            super(context, attrs) { onInit() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) { onInit() }

    private val deviceDimens
        get() = context.getScreenDimens()

    private val defaultMargin = resources.getDimensionPixelSize(R.dimen.md_margin)

    private val spanSize = resources.getInteger(R.integer.grid_list_x2)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = getSize(widthMeasureSpec)
        if (width == 0)
            width = deviceDimens.x / spanSize - defaultMargin

        val height = (width * PORTRAIT_ASPECT_RATIO)
        super.onMeasure(
            makeMeasureSpec(height.toInt(), EXACTLY),
            makeMeasureSpec(width, EXACTLY)
        )
    }

    /**
     * Callable in view constructors to perform view inflation and
     * additional attribute initialization
     */
    override fun onInit() {

    }

    /**
     * Should be called on a view's detach from window to unbind or
     * release object references, by default this method will
     * cancel all running coroutine jobs
     *
     * @see [io.wax911.support.core.util.SupportCoroutineHelper.cancelAllChildren]
     */
    override fun onViewRecycled() {
        super.onViewRecycled()
        with(this) {
            Glide.with(context)
                .clear(this)
        }
    }

    companion object {
        const val PORTRAIT_ASPECT_RATIO = 1.37f
    }
}