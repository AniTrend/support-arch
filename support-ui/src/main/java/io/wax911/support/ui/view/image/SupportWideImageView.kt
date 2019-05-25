package io.wax911.support.ui.view.image

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import io.wax911.support.core.view.contract.CustomView

/**
 * Wide image view with a 16:9 aspect ratio
 */
class SupportWideImageView : AppCompatImageView, CustomView {

    constructor(context: Context?) :
            super(context) { onInit() }
    constructor(context: Context?, attrs: AttributeSet?) :
            super(context, attrs) { onInit() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) { onInit() }


    //private val deviceDimens = resources.getDimensionPixelSize(R.dimen.app_bar_height)

    //private val defaultMargin = resources.getDimensionPixelSize(R.dimen.md_margin)

    /*override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = MeasureSpec.getSize(widthMeasureSpec)
        var width = ((MeasureSpec.getSize(heightMeasureSpec) - defaultMargin) * WIDE_ASPECT_RATIO)

        if (heightMeasureSpec == 0)
            width = (deviceDimens - defaultMargin) * WIDE_ASPECT_RATIO
        super.onMeasure(
            MeasureSpec.makeMeasureSpec(height.toInt(), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(width.toInt(), MeasureSpec.EXACTLY)
        )
    }*/

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
        const val WIDE_ASPECT_RATIO = 0.95f
    }
}