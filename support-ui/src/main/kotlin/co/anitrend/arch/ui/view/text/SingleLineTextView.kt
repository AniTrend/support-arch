package co.anitrend.arch.ui.view.text

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import co.anitrend.arch.ui.view.contract.CustomView

/**
 * Single line text view that truncates at the end of the string
 *
 * @since v0.9.X
 */
class SingleLineTextView : AppCompatTextView, CustomView {

    constructor(context: Context) :
            super(context) { onInit(context) }

    constructor(context: Context, attrs: AttributeSet?) :
            super(context, attrs) { onInit(context, attrs) }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) { onInit(context, attrs) }

    /**
     * Callable in view constructors to perform view inflation and attribute initialization
     *
     * @param context view context
     * @param attrs view attributes if applicable
     */
    override fun onInit(context: Context, attrs: AttributeSet?) {
        isSingleLine = true
        ellipsize = TextUtils.TruncateAt.END
    }
}
