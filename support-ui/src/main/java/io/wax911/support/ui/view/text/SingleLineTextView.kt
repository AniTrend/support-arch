package io.wax911.support.ui.view.text

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import io.wax911.support.ui.view.contract.CustomView

class SingleLineTextView : AppCompatTextView, CustomView {

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
        setSingleLine(true)
        ellipsize = TextUtils.TruncateAt.END
    }

    /**
     * Should be called on a view's detach from window to unbind or
     * release object references and cancel all running coroutine jobs if the current view
     * implements [io.wax911.support.extension.util.SupportCoroutineHelper]
     */
    override fun onViewRecycled() {

    }
}