package co.anitrend.arch.ui.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.ViewFlipper
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.domain.entities.RequestError
import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.arch.extension.coroutine.extension.Main
import co.anitrend.arch.extension.ext.getCompatDrawable
import co.anitrend.arch.extension.ext.getLayoutInflater
import co.anitrend.arch.extension.ext.gone
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.ui.R
import co.anitrend.arch.ui.view.widget.contract.ISupportStateLayout
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import kotlinx.android.synthetic.main.support_state_layout_error.view.*
import kotlinx.android.synthetic.main.support_state_layout_laoding.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber

/**
 * A state layout that supports [LoadState.Loading] and [LoadState.Error] states
 * by default by using a [ViewFlipper] as the underlying view
 *
 * @since v1.1.0
 */
class SupportStateLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewFlipper(context, attrs), ISupportStateLayout, ISupportCoroutine by Main() {

    init {
        onInit(context, attrs)
    }

    /**
     * Observable for click interactions, which returns the current network state
     */
    override val interactionFlow = MutableStateFlow<ClickableItem>(ClickableItem.None)

    /**
     * Observable for publishing states to this widget
     */
    override val loadStateFlow = MutableStateFlow<LoadState>(LoadState.Success())

    /**
     * Configuration for that should be used by the different view states
     */
    override val stateConfigFlow = MutableStateFlow<StateLayoutConfig?>(null)

    override val isLoading
        get() = displayedChild == ISupportStateLayout.LOADING_VIEW

    override val isError
        get() = displayedChild == ISupportStateLayout.ERROR_VIEW

    override val isContent
        get() = displayedChild == ISupportStateLayout.CONTENT_VIEW

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
    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        if (!isInEditMode)
            setupAdditionalViews()

        attrs?.apply {
            val a = context.obtainStyledAttributes(this, R.styleable.SupportStateLayout)
            displayedChild = a.getInt(R.styleable.SupportStateLayout_showState, ISupportStateLayout.CONTENT_VIEW)
            a.recycle()
        }

        stateLayoutErrorRetryAction.setOnClickListener {
            interactionFlow.value = ClickableItem.State(
                state = loadStateFlow.value,
                view = it
            )
        }
    }

    /**
     * Should be called on a view's detach from window to unbind or
     * release object references and cancel all running coroutine jobs if the current view
     */
    override fun onViewRecycled() {
        stateLayoutErrorRetryAction.setOnClickListener(null)
        interactionFlow.value = ClickableItem.None
    }

    @SuppressLint("InflateParams")
    private fun setupAdditionalViews() {
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
     * Updates the UI using the supplied [loadState]
     */
    private fun updateUsingLoadState(loadState: LoadState) {
        when (loadState) {
            is LoadState.Loading -> {
                if (!isLoading)
                    displayedChild = ISupportStateLayout.LOADING_VIEW
            }
            is LoadState.Error -> {
                if (loadState.details is RequestError) {
                    val requestError = loadState.details as RequestError
                    stateLayoutErrorHeading.text = requestError.topic
                    stateLayoutErrorMessage.text = requestError.message
                } else {
                    stateLayoutErrorHeading.text = loadState.details.javaClass.simpleName
                    stateLayoutErrorMessage.text = loadState.details.message
                }
                if (!isError)
                    displayedChild = ISupportStateLayout.ERROR_VIEW
            }
            is LoadState.Idle,
            is LoadState.Success -> {
                if (!isContent)
                    displayedChild = ISupportStateLayout.CONTENT_VIEW
            }
        }
        if (!isInLayout)
            requestLayout()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        launch {
            loadStateFlow
                .onEach {
                    updateUsingLoadState(it)
                }
                .catch { cause: Throwable ->
                    Timber.w(cause)
                }
                .collect()
        }
        launch {
            stateConfigFlow
                .filterNotNull()
                .onEach {
                    updateUsing(it)
                }
                .catch { cause: Throwable ->
                    Timber.w(cause)
                }
                .collect()
        }
    }
    
    override fun onDetachedFromWindow() {
        cancelAllChildren()
        onViewRecycled()
        super.onDetachedFromWindow()
    }
}