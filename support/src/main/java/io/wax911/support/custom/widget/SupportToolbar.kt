package io.wax911.support.custom.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import io.wax911.support.R
import io.wax911.support.base.view.CustomView
import io.wax911.support.getColorFromAttr

class SupportToolbar : Toolbar, CustomView {

    constructor(context: Context?)
            : super(context) { onInit() }
    constructor(context: Context?, attrs: AttributeSet?)
            : super(context, attrs) { onInit() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) { onInit() }

    /**
     * Optionally included when constructing custom views
     */
    override fun onInit() {
        setBackgroundColor(context.getColorFromAttr(R.attr.colorPrimary))
        popupTheme = R.style.SupportThemeLight_PopupOverlay
    }

    /**
     * Clean up any resources that won't be needed
     */
    override fun onViewRecycled() {

    }

    override fun onDetachedFromWindow() {
        onViewRecycled()
        super.onDetachedFromWindow()
    }
}
