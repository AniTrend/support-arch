package io.wax911.support.ui.view.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ViewFlipper
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
import timber.log.Timber

/**
 * A state layout that supports nesting of children using a frame layout
 */
class SupportStateLayout : ViewFlipper, CustomView {

    constructor(context: Context) :
            super(context) { onInit() }
    constructor(context: Context, attrs: AttributeSet?) :
            super(context, attrs) { onInit() }

    var isLoading = false
        private set

    /**
     * Callable in view constructors to perform view inflation and
     * additional attribute initialization
     */
    override fun onInit() {
        setInAnimation(context, android.R.anim.fade_in)
        setOutAnimation(context, android.R.anim.fade_out)
        context.getLayoutInflater().inflate(R.layout.support_layout_state, this, true)
    }

    fun showLoading(@DrawableRes drawableRes : Int = R.drawable.ic_support_empty_state, @StringRes loadingMessage: Int) {
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

    private fun onStateChanged(@SupportStateType state : Int) {
        Timber.tag(TAG).i("onStateChanged(@SupportStateType state : Int) -> Current displayed child $displayedChild")
        when (state) {
            SupportStateType.CONTENT -> {
                isLoading = false
                stateLinearContent.gone()
                if (displayedChild != DEFAULT_VIEW)
                    showNext()
            }
            SupportStateType.LOADING -> {
                isLoading = true
                stateLinearContent.visible()
                stateProgress.visible()
                if (displayedChild != DEFAULT_VIEW)
                    showPrevious()
            }
            else -> {
                isLoading = false
                stateLinearContent.visible()
                stateProgress.gone()
                if (displayedChild == DEFAULT_VIEW)
                    showPrevious()
            }
        }
    }

    companion object {
        const val DEFAULT_VIEW = 1
    }
}