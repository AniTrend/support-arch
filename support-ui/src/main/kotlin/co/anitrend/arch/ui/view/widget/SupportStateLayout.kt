package co.anitrend.arch.ui.view.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ViewFlipper
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.getCompatDrawable
import co.anitrend.arch.extension.getLayoutInflater
import co.anitrend.arch.ui.R
import co.anitrend.arch.ui.util.SupportStateLayoutConfiguration
import co.anitrend.arch.ui.view.contract.CustomView
import kotlinx.android.synthetic.main.support_state_layout_error.view.*
import kotlinx.android.synthetic.main.support_state_layout_laoding.view.*

/**
 * A state layout that supports [NetworkState.Loading] and [NetworkState.Error] states
 * by default by using a [ViewFlipper] as the underlying view
 *
 * @since v1.1.0
 */
open class SupportStateLayout : ViewFlipper, CustomView {

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
                setInAnimation(context, inAnimation)
                setOutAnimation(context, outAnimation)

                stateLayoutErrorRetryAction.setText(retryAction)
                stateLayoutErrorImage.setImageDrawable(
                    context.getCompatDrawable(errorDrawable)
                )

                stateLayoutLoadingText.setText(loadingMessage)
                stateLayoutLoadingImage.setImageDrawable(
                    context.getCompatDrawable(loadingDrawable)
                )
            }
        }

    /**
     * Checks if the current view state is loading
     */
    val isLoading
        get() = displayedChild == LOADING_VIEW

    val isError
        get() = displayedChild == ERROR_VIEW

    val isContent
        get() = displayedChild == CONTENT_VIEW

    var onWidgetInteraction: OnClickListener? = null
        set(value) {
            field = value
            stateLayoutErrorRetryAction.setOnClickListener(field)
        }

    /**
     * Callable in view constructors to perform view inflation and
     * additional attribute initialization
     */
    final override fun onInit(context: Context, attrs: AttributeSet?) {
        if (!isInEditMode)
            setupAdditionalViews()

        attrs?.apply {
            val a = context.obtainStyledAttributes(this, R.styleable.SupportStateLayout)
            runCatching {
                displayedChild = a.getInt(R.styleable.SupportStateLayout_showState, CONTENT_VIEW)
            }.exceptionOrNull()?.printStackTrace()
            a.recycle()
        }
    }

    /**
     * Should be called on a view's detach from window to unbind or
     * release object references and cancel all running coroutine jobs if the current view
     */
    override fun onViewRecycled() {
        onWidgetInteraction = null
    }

    protected open fun setupAdditionalViews() {
        val loadingView = getLayoutInflater().inflate(
            R.layout.support_state_layout_laoding, null
        )
        addView(loadingView)

        val errorView = getLayoutInflater().inflate(
            R.layout.support_state_layout_error, null
        )
        addView(errorView)
    }

    /**
     * Changes the layout state based on [NetworkState]
     *
     * @param networkState state to use
     */
    open fun setNetworkState(networkState: NetworkState) {
        when (networkState) {
            is NetworkState.Loading -> {
                if (!isLoading)
                    displayedChild = LOADING_VIEW
            }
            is NetworkState.Error -> {
                stateLayoutErrorHeading.text = networkState.heading
                stateLayoutErrorMessage.text = networkState.message
                if (!isError)
                    displayedChild = ERROR_VIEW
                requestLayout()
            }
            is NetworkState.Success -> {
                if (!isContent)
                    displayedChild = CONTENT_VIEW
            }
        }
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