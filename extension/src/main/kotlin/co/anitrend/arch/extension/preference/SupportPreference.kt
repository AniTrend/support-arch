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

package co.anitrend.arch.extension.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import co.anitrend.arch.extension.preference.contract.ISupportPreference
import co.anitrend.arch.extension.settings.contract.AbstractSetting

/**
 * Core abstract implementation for application preferences
 *
 * @since v0.9.X
 *
 * @see AbstractSetting
 */
abstract class SupportPreference
    @JvmOverloads
    constructor(
        protected val context: Context,
        sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context),
    ) : ISupportPreference, SharedPreferences by sharedPreferences
