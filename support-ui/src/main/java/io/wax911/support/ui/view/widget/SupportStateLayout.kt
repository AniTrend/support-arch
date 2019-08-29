package io.wax911.support.ui.view.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ViewFlipper
import io.wax911.support.data.model.NetworkState
import io.wax911.support.extension.getCompatDrawable
import io.wax911.support.extension.getLayoutInflater
import io.wax911.support.ui.R
import io.wax911.support.ui.util.SupportStateLayoutConfiguration
import io.wax911.support.ui.view.contract.CustomView
import kotlinx.android.synthetic.main.support_state_layout_laoding.view.*
import kotlinx.android.synthetic.main.support_state_layout_error.view.*

/**
 * A state layout that supports nesting of children using a frame layout
 */
class SupportStateLayout : ViewFlipper, CustomView {

    constructor(context: Context) :
            super(context) { onInit(context) }
    constructor(context: Context, attrs: AttributeSet?) :
            super(context, attrs) { onInit(context, attrs) }

    /**
     * Configuration for that should be used by the different view states
     */
    var stateConfiguration: SupportStateLayoutConfiguration? = null
        set(value) {
            field = value
            field?.apply {
                stateLayoutErrorRetryAction.setText(retryAction)
                stateLayoutErrorImage.setImageDrawable(
                    context.getCompatDrawable(errorDrawable)
                )

                stateLayoutLoadingText.setText(loadingMessage)
                stateLayoutLoadingImage.setImageDrawable(
                    context.getCompatDrawable(errorDrawable)
                )
            }
        }

    /**
     * Checks if the current view state is loading
     */
    val isLoading
        get() = displayedChild == LOADING_VIEW

    var onWidgetInteraction: OnClickListener? = null
        set(value) {
            field = value
            stateLayoutErrorRetryAction.setOnClickListener(field)
        }

    /**
     * Callable in view constructors to perform view inflation and
     * additional attribute initialization
     */
    override fun onInit(context: Context, attrs: AttributeSet?) {
        setInAnimation(context, android.R.anim.fade_in)
        setOutAnimation(context, android.R.anim.fade_out)
        setupAdditionalViews()
    }

    /**
     * Should be called on a view's detach from window to unbind or
     * release object references and cancel all running coroutine jobs if the current view
     */
    override fun onViewRecycled() {
        onWidgetInteraction = null
    }

    private fun setupAdditionalViews() {
        val loadingView = getLayoutInflater().inflate(R.layout.support_state_layout_laoding, null)
        addView(loadingView)

        val errorView = getLayoutInflater().inflate(R.layout.support_state_layout_error, null)
        addView(errorView)
    }

    /**
     * Changes the layout state based on [NetworkState]
     *
     * @param networkState state to use
     */
    fun setNetworkState(networkState: NetworkState) {
        when (networkState) {
            is NetworkState.Loading -> {
                displayedChild = LOADING_VIEW
            }
            is NetworkState.Error -> {
                stateLayoutErrorHeading.text = networkState.heading
                stateLayoutErrorMessage.text = networkState.message
                displayedChild = ERROR_VIEW
            }
            is NetworkState.Success ->
                displayedChild = CONTENT_VIEW
        }
        // TODO: not sure if we really need to request view to redraw/invalidate
        // requestLayout()
    }

    companion object {
        /** First view inflated index which is loading view */
        const val LOADING_VIEW = 0
        /** Second view inflated in this case the error view */
        const val ERROR_VIEW = 1
        /** Third inflated view should be the current view wrapped by this layout */
        const val CONTENT_VIEW = 2
    }
}