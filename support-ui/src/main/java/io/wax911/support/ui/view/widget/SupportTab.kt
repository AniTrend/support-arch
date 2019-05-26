package io.wax911.support.ui.view.widget

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.tabs.TabLayout
import io.wax911.support.ui.view.contract.CustomView
import io.wax911.support.extension.getColorFromAttr
import io.wax911.support.ui.R

class SupportTab: TabLayout, CustomView {

    constructor(context: Context?) :
            super(context) { onInit() }
    constructor(context: Context?, attrs: AttributeSet?) :
            super(context, attrs) { onInit() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) { onInit() }


    /**
     * Callable in view constructors to perform view inflation and
     * additional attribute initialization
     */
    override fun onInit() {
        setBackgroundColor(context.getColorFromAttr(R.attr.colorPrimary))
    }

}