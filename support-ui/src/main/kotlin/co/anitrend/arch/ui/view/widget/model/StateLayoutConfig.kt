/**
 * Copyright 2021 AniTrend
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.anitrend.arch.ui.view.widget.model

import androidx.annotation.AnimRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import co.anitrend.arch.core.model.IStateLayoutConfig

/**
 * Configurable state layout params to be consumed by
 * [co.anitrend.arch.ui.view.widget.SupportStateLayout]
 *
 * @param loadingDrawable drawable for loading state, set this to null if you wish to hide the element
 * @param errorDrawable drawable for error state, set this to null if you wish to hide the element
 * @param loadingMessage label for loading message, set this to null if you wish to hide the element
 * @property defaultMessage label for success state, set this to null if you wish to hide the element
 * @param retryAction label for retry action, set this to null if you wish to hide the element
 * @param inAnimation optional animations to use for switching in views
 * @param outAnimation optional animations to use for switching out views
 *
 * @since v1.2.0
 */
data class StateLayoutConfig(
    @DrawableRes override val loadingDrawable: Int? = null,
    @DrawableRes override val errorDrawable: Int? = null,
    @StringRes override val loadingMessage: Int? = null,
    @StringRes override val defaultMessage: Int? = null,
    @StringRes override val retryAction: Int? = null,
    @AnimRes override val inAnimation: Int = android.R.anim.fade_in,
    @AnimRes override val outAnimation: Int = android.R.anim.fade_out,
) : IStateLayoutConfig
