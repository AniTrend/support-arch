package io.wax911.support.ui.view.image

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import io.wax911.support.ui.R
import io.wax911.support.ui.view.contract.CustomView

/**
 * Created by max on 2017/10/30.
 *
 * Custom image view which can be set up to fit portrait and landscape use cases.
 *
 * This widget also support defined references like [R.integer.grid_list_x2]
 * instead of just raw values
 */
class SupportImageView : AppCompatImageView, CustomView {

    protected var aspectRatio = DEFAULT_ASPECT_RATIO

    constructor(context: Context) :
            super(context) { onInit(context) }
    constructor(context: Context, attrs: AttributeSet?) :
            super(context, attrs) { onInit(context, attrs) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) { onInit(context, attrs) }

    /**
     * Callable in view constructors to perform view inflation and
     * additional attribute initialization
     */
    override fun onInit(context: Context, attrs: AttributeSet?) {
        attrs?.apply {
            val a = context.obtainStyledAttributes(this, R.styleable.SupportImageView)
            with(a) {
                aspectRatio = getFloat(R.styleable.SupportImageView_aspectRatio, DEFAULT_ASPECT_RATIO)
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var width = measuredWidth
        var height = measuredHeight

        if (width == 0 && height == 0)
            return

        when (width > 0) {
            true -> height = (width * aspectRatio).toInt()
            else -> width = (height / aspectRatio).toInt()
        }

        setMeasuredDimension(width, height)
    }

    /**
     * Should be called on a view's detach from window to unbind or
     * release object references and cancel all running coroutine jobs if the current view
     * implements [io.wax911.support.extension.util.SupportCoroutineHelper]
     */
    override fun onViewRecycled() {
        Glide.with(context).clear(this)
    }

    companion object {
        const val DEFAULT_ASPECT_RATIO = 1f
    }
}