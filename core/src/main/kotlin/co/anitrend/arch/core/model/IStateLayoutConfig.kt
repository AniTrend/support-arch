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
 * @property defaultMessage label for success state, set this to null if you wish to hide the element
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
    val defaultMessage: Int?
    @get:StringRes
    val retryAction: Int?
    @get:AnimRes
    val inAnimation: Int
    @get:AnimRes
    val outAnimation: Int
}
