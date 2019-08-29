package io.wax911.support.ui.view.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ViewFlipper
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import io.wax911.support.data.model.NetworkState
import io.wax911.support.data.model.contract.SupportStateContract
import io.wax911.support.extension.getCompatDrawable
import io.wax911.support.extension.getLayoutInflater
import io.wax911.support.extension.gone
import io.wax911.support.extension.visible
import io.wax911.support.ui.R
import io.wax911.support.ui.view.contract.CustomView
import kotlinx.android.synthetic.main.support_layout_state.view.*

/**
 * A state layout that supports nesting of children using a frame layout
 */
class SupportStateLayout : ViewFlipper, CustomView {

    constructor(context: Context) :
            super(context) { onInit(context) }
    constructor(context: Context, attrs: AttributeSet?) :
            super(context, attrs) { onInit(context, attrs) }

    var isLoading = false
        private set(value) {
            field = value
            if (value)
                stateProgress.visible()
            else
                stateProgress.gone()
            requestLayout()
        }

    var onWidgetInteraction: OnClickListener? = null
        set(value) {
            field = value
            stateLinearContent.setOnClickListener(field)
        }

    /**
     * Callable in view constructors to perform view inflation and
     * additional attribute initialization
     */
    override fun onInit(context: Context, attrs: AttributeSet?) {
        setInAnimation(context, android.R.anim.fade_in)
        setOutAnimation(context, android.R.anim.fade_out)
        getLayoutInflater().inflate(R.layout.support_layout_state, this, true)
    }

    /**
     * Should be called on a view's detach from window to unbind or
     * release object references and cancel all running coroutine jobs if the current view
     */
    override fun onViewRecycled() {
        onWidgetInteraction = null
    }

    fun showLoading(@DrawableRes drawableRes : Int = R.drawable.ic_support_empty_state, @StringRes loadingMessage: Int) {
        stateImage.setImageDrawable(context.getCompatDrawable(drawableRes))
        stateText.text = context.getString(loadingMessage)
        onStateChanged(NetworkState.LOADING)
    }

    fun showContent() = onStateChanged(NetworkState.LOADED)

    fun showError(@DrawableRes drawableRes : Int = R.drawable.ic_support_empty_state, @StringRes errorMessage: Int) {
        stateImage.setImageDrawable(context.getCompatDrawable(drawableRes))
        stateText.text = context.getString(errorMessage)
        onStateChanged(NetworkState.error(context.getString(errorMessage)))
    }

    fun showError(@DrawableRes drawableRes : Int = R.drawable.ic_support_empty_state, errorMessage: String?) {
        stateImage.setImageDrawable(context.getCompatDrawable(drawableRes))
        stateText.text = errorMessage
        onStateChanged(NetworkState.error(errorMessage))
    }

    private fun onStateChanged(networkState: NetworkState) {
        when (networkState.status) {
            SupportStateContract.CONTENT -> {
                isLoading = false
                if (displayedChild != DEFAULT_VIEW)
                    showNext()
            }
            SupportStateContract.LOADING -> {
                isLoading = true
                if (displayedChild != DEFAULT_VIEW)
                    showPrevious()
            }
            else -> {
                isLoading = false
                if (displayedChild == DEFAULT_VIEW)
                    showPrevious()
            }
        }
    }

    companion object {
        const val DEFAULT_VIEW = 1
    }
}