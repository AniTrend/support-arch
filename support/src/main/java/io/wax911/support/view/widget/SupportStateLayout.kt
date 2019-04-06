package io.wax911.support.view.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import io.wax911.support.*
import io.wax911.support.extension.getCompatDrawable
import io.wax911.support.extension.getLayoutInflater
import io.wax911.support.extension.gone
import io.wax911.support.extension.visible
import io.wax911.support.view.contract.CustomView
import kotlinx.android.synthetic.main.support_layout_state.view.*

class SupportStateLayout : FrameLayout, CustomView {

    constructor(context: Context) :
            super(context) { onInit() }
    constructor(context: Context, attrs: AttributeSet?) :
            super(context, attrs) { onInit() }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) { onInit() }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes) { onInit() }

    /**
     * Callable in view constructors to perform view inflation and
     * additional attribute initialization
     */
    override fun onInit() {
        context.getLayoutInflater().inflate(R.layout.support_layout_state, this, true)
    }

    fun showLoading(@DrawableRes drawableRes : Int = R.drawable.ic_support_empty_state, @StringRes loadingMessage: Int) {
        stateImage.setImageDrawable(context.getCompatDrawable(drawableRes))
        stateText.text = context.getString(loadingMessage)
        onStateChanged(STATE_LOADING)
    }

    fun showContent() = onStateChanged(STATE_CONTENT)

    fun showError(@DrawableRes drawableRes : Int = R.drawable.ic_support_empty_state, @StringRes errorMessage: Int, onClickListener: OnClickListener) {
        stateImage.setImageDrawable(context.getCompatDrawable(drawableRes))
        stateLinearContent.setOnClickListener(onClickListener)
        stateText.text = context.getString(errorMessage)
        onStateChanged(STATE_ERROR)
    }

    fun showError(@DrawableRes drawableRes : Int = R.drawable.ic_support_empty_state, errorMessage: String?, onClickListener: OnClickListener) {
        stateImage.setImageDrawable(context.getCompatDrawable(drawableRes))
        stateLinearContent.setOnClickListener(onClickListener)
        stateText.text = errorMessage
        onStateChanged(STATE_ERROR)
    }

    private fun onStateChanged(state : Int) = when (state) {
        STATE_CONTENT -> stateLinearContent.gone()
        STATE_LOADING -> {
            stateLinearContent.visible()
            stateProgress.visible()
        }
        else -> {
            stateLinearContent.visible()
            stateProgress.gone()
        }
    }

    companion object {
        private const val STATE_LOADING = 0
        private const val STATE_CONTENT = 1
        private const val STATE_ERROR = 2
    }
}