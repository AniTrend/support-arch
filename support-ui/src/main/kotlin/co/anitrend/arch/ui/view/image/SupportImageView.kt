package co.anitrend.arch.ui.view.image

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import co.anitrend.arch.extension.ext.use
import co.anitrend.arch.ui.R
import co.anitrend.arch.ui.view.contract.CustomView

/**
 *
 * Custom image view which can be set up to fit portrait and landscape use cases.
 *
 * This widget also support defined references like [R.integer.grid_list_x2]
 * instead of just raw values
 *
 * @since v0.9.X
 */
open class SupportImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr), CustomView {

    protected open var aspectRatio = DEFAULT_ASPECT_RATIO

    init {
        onInit(context, attrs, defStyleAttr)
    }

    /**
     * Callable in view constructors to perform view inflation and
     * additional attribute initialization
     */
    @SuppressLint("Recycle")
    final override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        if (attrs != null) {
            context.obtainStyledAttributes(attrs, R.styleable.SupportImageView).use {
                aspectRatio = it.getFloat(
                    R.styleable.SupportImageView_aspectRatio,
                    DEFAULT_ASPECT_RATIO
                )
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
     * Should be called on a view's detach from window to unbind or release object references
     * and cancel all running coroutine jobs if the current view
     *
     * Consider calling this in [android.view.View.onDetachedFromWindow]
     */
    override fun onViewRecycled() {
        if (hasOnClickListeners())
            setOnClickListener(null)
    }

    override fun onDetachedFromWindow() {
        onViewRecycled()
        super.onDetachedFromWindow()
    }

    companion object {
        const val DEFAULT_ASPECT_RATIO = 1f
    }
}