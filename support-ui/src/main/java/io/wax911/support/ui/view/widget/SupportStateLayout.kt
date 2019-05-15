package io.wax911.support.ui.view.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.annotation.StringRes
import io.wax911.support.extension.getCompatDrawable
import io.wax911.support.extension.getLayoutInflater
import io.wax911.support.extension.gone
import io.wax911.support.extension.visible
import io.wax911.support.core.view.contract.CustomView
import io.wax911.support.core.view.model.contract.SupportStateType
import io.wax911.support.ui.R
import kotlinx.android.synthetic.main.support_layout_state.view.*

/**
 * A state layout that supports nesting of children using a frame layout
 *
 * TODO: Implement view flipper as the base parent
 */
class SupportStateLayout : FrameLayout, CustomView {

    constructor(context: Context) :
            super(context) { onInit() }
    constructor(context: Context, attrs: AttributeSet?) :
            super(context, attrs) { onInit() }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) { onInit() }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes) { onInit() }

    var isLoading = false
        set(value) {
            field = value
            // Does some interesting things
            if (value) showLoading()
            else showContent()
        }

    /**
     * Callable in view constructors to perform view inflation and
     * additional attribute initialization
     */
    override fun onInit() {
        context.getLayoutInflater().inflate(R.layout.support_layout_state, this, true)
    }

    fun showLoading(@DrawableRes drawableRes : Int = R.drawable.ic_support_empty_state, @StringRes loadingMessage: Int = 0) {
        stateImage.setImageDrawable(context.getCompatDrawable(drawableRes))
        stateText.text = context.getString(loadingMessage)
        onStateChanged(SupportStateType.LOADING)
    }

    fun showContent() = onStateChanged(SupportStateType.CONTENT)

    fun showError(@DrawableRes drawableRes : Int = R.drawable.ic_support_empty_state, @StringRes errorMessage: Int, onClickListener: OnClickListener) {
        stateImage.setImageDrawable(context.getCompatDrawable(drawableRes))
        stateLinearContent.setOnClickListener(onClickListener)
        stateText.text = context.getString(errorMessage)
        onStateChanged(SupportStateType.ERROR)
    }

    fun showError(@DrawableRes drawableRes : Int = R.drawable.ic_support_empty_state, errorMessage: String?, onClickListener: OnClickListener) {
        stateImage.setImageDrawable(context.getCompatDrawable(drawableRes))
        stateLinearContent.setOnClickListener(onClickListener)
        stateText.text = errorMessage
        onStateChanged(SupportStateType.ERROR)
    }

    private fun onStateChanged(@SupportStateType state : Int) = when (state) {
        SupportStateType.CONTENT -> {
            stateLinearContent.gone()
        }
        SupportStateType.LOADING -> {
            stateLinearContent.visible()
            stateProgress.visible()
        }
        else -> {
            stateLinearContent.visible()
            stateProgress.gone()
        }
    }
}