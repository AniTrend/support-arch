package io.wax911.support.custom.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import io.wax911.support.base.view.CustomView

class SingleLineTextView : AppCompatTextView, CustomView {

    constructor(context: Context?) :
            super(context) { onInit() }
    constructor(context: Context?, attrs: AttributeSet?) :
            super(context, attrs) { onInit() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) { onInit() }

    /**
     * Optionally included when constructing custom views
     */
    override fun onInit() {
        setSingleLine(true)
        ellipsize = TextUtils.TruncateAt.END
    }

    /**
     * Clean up any resources that won't be needed
     */
    override fun onViewRecycled() {

    }

    /**
     * This is called when the view is detached from a window.  At this point it
     * no longer has a surface for drawing.
     *
     * @see .onAttachedToWindow
     */
    override fun onDetachedFromWindow() {
        onViewRecycled()
        super.onDetachedFromWindow()
    }
}