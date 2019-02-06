package io.wax911.support.view.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import io.wax911.support.R
import io.wax911.support.extension.getColorFromAttr
import io.wax911.support.view.contract.CustomView

class SupportToolbar : Toolbar, CustomView {

    constructor(context: Context?)
            : super(context) { onInit() }
    constructor(context: Context?, attrs: AttributeSet?)
            : super(context, attrs) { onInit() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) { onInit() }

    /**
     * Callable in view constructors to perform view inflation and
     * additional attribute initialization
     */
    override fun onInit() {
        setBackgroundColor(context.getColorFromAttr(R.attr.colorPrimary))
    }
}
