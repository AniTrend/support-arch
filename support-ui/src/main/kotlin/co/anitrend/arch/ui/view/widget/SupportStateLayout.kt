package co.anitrend.arch.ui.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ViewFlipper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.getCompatDrawable
import co.anitrend.arch.extension.getLayoutInflater
import co.anitrend.arch.extension.gone
import co.anitrend.arch.ui.R
import co.anitrend.arch.ui.util.StateLayoutConfig
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
    var stateConfig: StateLayoutConfig? = null
        set(value) {
            field = value?.also { updateUsing(it) }
        }

    val isLoading
        get() = displayedChild == LOADING_VIEW

    val isError
        get() = displayedChild == ERROR_VIEW

    val isContent
        get() = displayedChild == CONTENT_VIEW

    private val _interactionLiveData = MutableLiveData<View>()
    val interactionLiveData: LiveData<View>
        get() = _interactionLiveData

    @Deprecated("Preferably use [SupportStateLayout.interactionLiveData]")
    var onWidgetInteraction: OnClickListener? = null

    private fun updateUsing(config: StateLayoutConfig) {
        setInAnimation(context, config.inAnimation)
        setOutAnimation(context, config.outAnimation)
        if (config.errorDrawable != null)
            stateLayoutErrorImage.setImageDrawable(
                context.getCompatDrawable(config.errorDrawable)
            )
        else
            stateLayoutErrorImage.gone()

        if (config.loadingDrawable != null)
            stateLayoutLoadingImage.setImageDrawable(
                context.getCompatDrawable(config.loadingDrawable)
            )
        else
            stateLayoutLoadingImage.gone()

        if (config.retryAction != null)
            stateLayoutErrorRetryAction.setText(config.retryAction)
        else
            stateLayoutErrorRetryAction.gone()

        if (config.loadingMessage != null)
            stateLayoutLoadingText.setText(config.loadingMessage)
        else
            stateLayoutLoadingText.gone()
    }

    /**
     * Callable in view constructors to perform view inflation and
     * additional attribute initialization
     */
    final override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        if (!isInEditMode)
            setupAdditionalViews()

        attrs?.apply {
            val a = context.obtainStyledAttributes(this, R.styleable.SupportStateLayout)
            displayedChild = a.getInt(R.styleable.SupportStateLayout_showState, CONTENT_VIEW)
            a.recycle()
        }

        stateLayoutErrorRetryAction.setOnClickListener {
            _interactionLiveData.postValue(it)
            onWidgetInteraction?.onClick(it)
        }
    }

    /**
     * Should be called on a view's detach from window to unbind or
     * release object references and cancel all running coroutine jobs if the current view
     */
    override fun onViewRecycled() {
        onWidgetInteraction = null
    }

    @SuppressLint("InflateParams")
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
            }
            is NetworkState.Success -> {
                if (!isContent)
                    displayedChild = CONTENT_VIEW
            }
        }
        if (!isInLayout)
            requestLayout()
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