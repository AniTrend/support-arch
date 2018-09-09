package io.wax911.support.custom.widget

import android.annotation.TargetApi
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.widget.ProgressBar
import io.wax911.support.R
import io.wax911.support.base.view.CustomView
import io.wax911.support.getCompatColor

class CustomProgress : ProgressBar, CustomView {

    private var mColorFilter: PorterDuffColorFilter? = null

    constructor(context: Context?) :
            super(context) { onInit() }
    constructor(context: Context?, attrs: AttributeSet?) :
            super(context, attrs) { onInit() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) { onInit() }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes) { onInit() }

    /**
     * Optionally included when constructing custom views
     */
    override fun onInit() {
        mColorFilter = PorterDuffColorFilter(context.getCompatColor(R.color.colorAccent),
                PorterDuff.Mode.SRC_IN)
        applyColorFilter(progressDrawable)
        applyColorFilter(indeterminateDrawable)
    }

    /**
     * Clean up any resources that won't be needed
     */
    override fun onViewRecycled() {
        mColorFilter = null
    }

    override fun onDetachedFromWindow() {
        onViewRecycled()
        super.onDetachedFromWindow()
    }

    private fun applyColorFilter(drawable: Drawable?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && drawable != null)
            drawable.colorFilter = mColorFilter
    }

    override fun setProgressDrawable(drawable: Drawable) {
        applyColorFilter(drawable)
        super.setProgressDrawable(drawable)
    }

    override fun setIndeterminateDrawable(drawable: Drawable) {
        applyColorFilter(drawable)
        super.setIndeterminateDrawable(drawable)
    }
}