package co.anitrend.arch.ui.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.ViewFlipper
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.arch.extension.coroutine.extension.Main
import co.anitrend.arch.extension.ext.getCompatDrawable
import co.anitrend.arch.extension.ext.getLayoutInflater
import co.anitrend.arch.extension.ext.gone
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.ui.R
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import kotlinx.android.synthetic.main.support_state_layout_error.view.*
import kotlinx.android.synthetic.main.support_state_layout_laoding.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber

/**
 * A state layout that supports [NetworkState.Loading] and [NetworkState.Error] states
 * by default by using a [ViewFlipper] as the underlying view
 *
 * @since v1.1.0
 */
open class SupportStateLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewFlipper(context, attrs), CustomView, ISupportCoroutine by Main() {

    init {
        onInit(context, attrs)
    }

    private val moduleTag = javaClass.simpleName

    private val interactionMutableStateFlow =
        MutableStateFlow<ClickableItem.State?>(null)

    /**
     * Observable for click interactions, which returns the current network state
     */
    val interactionStateFlow: StateFlow<ClickableItem.State?> =
        interactionMutableStateFlow

    /**
     * Observable for publishing states to this widget
     */
    val networkMutableStateFlow =
        MutableStateFlow<NetworkState>(NetworkState.Success)

    private val networkStateFlow: StateFlow<NetworkState> = networkMutableStateFlow

    /**
     * Configuration for that should be used by the different view states
     */
    val stateConfigFlow =
        MutableStateFlow<StateLayoutConfig?>(null)

    open val isLoading
        get() = displayedChild == LOADING_VIEW

    open val isError
        get() = displayedChild == ERROR_VIEW

    open val isContent
        get() = displayedChild == CONTENT_VIEW

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
    @Suppress("EXPERIMENTAL_API_USAGE")
    final override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        if (!isInEditMode)
            setupAdditionalViews()

        attrs?.apply {
            val a = context.obtainStyledAttributes(this, R.styleable.SupportStateLayout)
            displayedChild = a.getInt(R.styleable.SupportStateLayout_showState, CONTENT_VIEW)
            a.recycle()
        }

        stateLayoutErrorRetryAction.setOnClickListener {
            interactionMutableStateFlow.value =
                ClickableItem.State(
                    state = networkMutableStateFlow.value,
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
        interactionMutableStateFlow.value = null
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
    @Deprecated(
        "Use networkMutableStateFlow directly to inform this control about changes",
        ReplaceWith("networkMutableStateFlow.value = "),
        DeprecationLevel.ERROR
    )
    open fun setNetworkState(networkState: NetworkState) {
        networkMutableStateFlow.value = networkState
    }

    /**
     * Updates the UI using the supplied [networkState]
     */
    protected open fun updateUsingNetworkState(networkState: NetworkState) {
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
            is NetworkState.Success, NetworkState.Idle -> {
                if (!isContent)
                    displayedChild = CONTENT_VIEW
            }
        }
        if (!isInLayout)
            requestLayout()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        launch {
            networkStateFlow
                .onEach {
                    updateUsingNetworkState(it)
                }
                .catch { cause: Throwable ->
                    Timber.tag(moduleTag).w(cause)
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
                    Timber.tag(moduleTag).w(cause)
                }
                .collect()
        }
    }
    
    override fun onDetachedFromWindow() {
        cancelAllChildren()
        onViewRecycled()
        super.onDetachedFromWindow()
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