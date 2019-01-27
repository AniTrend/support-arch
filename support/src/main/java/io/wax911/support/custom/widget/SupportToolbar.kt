package io.wax911.support.custom.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import io.wax911.support.R
import io.wax911.support.view.CustomView
import io.wax911.support.custom.activity.SupportActivity
import io.wax911.support.custom.presenter.SupportPresenter
import io.wax911.support.getColorFromAttr
import io.wax911.support.isLightTheme

class SupportToolbar : Toolbar, CustomView {

    private var presenter: SupportPresenter<*>? = null

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
        if (context is SupportActivity<*, *>)
            presenter = (context as SupportActivity<* , *>).initPresenter()

        setBackgroundColor(context.getColorFromAttr(R.attr.colorPrimary))

        val isLightTheme = presenter?.let {
            it.supportPreference?.getTheme()?.isLightTheme()
        }
        popupTheme = when (isLightTheme) {
            false -> R.style.SupportThemeDark_PopupOverlay
            null, true -> R.style.SupportThemeLight_PopupOverlay
        }
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
