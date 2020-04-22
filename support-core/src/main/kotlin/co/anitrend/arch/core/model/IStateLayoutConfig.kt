package co.anitrend.arch.core.model

import androidx.annotation.AnimRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Contract for state layout configuration
 *
 * @property loadingDrawable drawable for loading state, set this to null if you wish to hide the element
 * @property errorDrawable drawable for error state, set this to null if you wish to hide the element
 * @property loadingMessage label for loading message, set this to null if you wish to hide the element
 * @property retryAction label for retry action, set this to null if you wish to hide the element
 * @property inAnimation optional animations to use for switching in views
 * @property outAnimation optional animations to use for switching out views
 *
 * @since v1.3.0
*/
interface IStateLayoutConfig {
    @get:DrawableRes
    val loadingDrawable: Int?
    @get:DrawableRes
    val errorDrawable: Int?
    @get:StringRes
    val loadingMessage: Int?
    @get:StringRes
    val retryAction: Int?
    @get:AnimRes
    val inAnimation: Int
    @get:AnimRes
    val outAnimation: Int
}