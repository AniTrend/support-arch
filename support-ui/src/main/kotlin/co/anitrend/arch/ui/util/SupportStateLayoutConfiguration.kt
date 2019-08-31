package co.anitrend.arch.ui.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Configurable state layout params
 */
data class SupportStateLayoutConfiguration(
    @DrawableRes val loadingDrawable: Int,
    @DrawableRes val errorDrawable: Int,
    @StringRes val loadingMessage: Int,
    @StringRes val retryAction: Int
)