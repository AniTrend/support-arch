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

package co.anitrend.arch.core.presenter

import android.content.Context
import co.anitrend.arch.core.presenter.contract.ISupportPresenter
import co.anitrend.arch.extension.lifecycle.SupportLifecycle
import co.anitrend.arch.extension.preference.SupportPreference

/**
 * An abstract declaration of what responsibilities a presenter should undertake
 *
 * @param context application based context
 * @param settings implementation of application preferences
 *
 * @see SupportPreference
 *
 * @since v0.9.X
 */
abstract class SupportPresenter<S : SupportPreference>(
    protected val context: Context,
    override val settings: S
) : ISupportPresenter, SupportLifecycle
