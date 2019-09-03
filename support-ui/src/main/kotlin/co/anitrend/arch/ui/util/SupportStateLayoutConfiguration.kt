package co.anitrend.arch.ui.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Configurable state layout params to be consumed by
 * [co.anitrend.arch.ui.view.widget.SupportStateLayout]
 *
 * @param loadingDrawable drawable for loading state
 * @param errorDrawable drawable for error state
 * @param loadingMessage label for loading message
 * @param retryAction label for retry action
 *
 * @since v1.2.0
 */
data class SupportStateLayoutConfiguration(
    @DrawableRes val loadingDrawable: Int,
    @DrawableRes val errorDrawable: Int,
    @StringRes val loadingMessage: Int,
    @StringRes val retryAction: Int
)