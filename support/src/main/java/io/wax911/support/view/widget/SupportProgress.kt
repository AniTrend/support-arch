package io.wax911.support.view.widget

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ProgressBar
import io.wax911.support.R
import io.wax911.support.extension.getColorFromAttr
import io.wax911.support.view.contract.CustomView

class SupportProgress : ProgressBar, CustomView {

    private val colorFilter: PorterDuffColorFilter? by lazy {
        PorterDuffColorFilter(context.getColorFromAttr(R.attr.colorAccent),
                PorterDuff.Mode.SRC_IN)
    }

    constructor(context: Context?) :
            super(context) { onInit() }
    constructor(context: Context?, attrs: AttributeSet?) :
            super(context, attrs) { onInit() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) { onInit() }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes) { onInit() }

    /**
     * Callable in view constructors to perform view inflation and
     * additional attribute initialization
     */
    override fun onInit() {
        progressDrawable?.colorFilter = colorFilter
        indeterminateDrawable?.colorFilter = colorFilter
    }

    override fun onDetachedFromWindow() {
        onViewRecycled()
        super.onDetachedFromWindow()
    }

    override fun setProgressDrawable(drawable: Drawable?) {
        drawable?.colorFilter = colorFilter
        super.setProgressDrawable(drawable)
    }

    override fun setIndeterminateDrawable(drawable: Drawable?) {
        drawable?.colorFilter = colorFilter
        super.setIndeterminateDrawable(drawable)
    }
}