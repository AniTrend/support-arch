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

package co.anitrend.arch.ui.common

import android.os.Bundle
import co.anitrend.arch.core.model.ISupportViewModelState

/**
 * Contract for implementing lifecycle owner controllers such as
 * [androidx.appcompat.app.AppCompatActivity] and
 * [androidx.fragment.app.Fragment]
 *
 * @since v1.3.0
 */
interface ILifecycleController {
    /**
     * Additional initialization to be done in this method, this is called in during
     * [androidx.fragment.app.FragmentActivity.onPostCreate]
     *
     * @param savedInstanceState
     */
    fun initializeComponents(savedInstanceState: Bundle?)

    /**
     * Proxy for a view model state if one exists
     */
    fun viewModelState(): ISupportViewModelState<*>?
}
