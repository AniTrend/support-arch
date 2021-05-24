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

package co.anitrend.arch.extension.settings.contract

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow

/**
 * An abstraction of a setting that is both observable and stateful
 *
 * @param preference Shared preference that will be used for persistence
 * @param default Default value to return
 */
abstract class AbstractSetting<T>(
    internal val preference: SharedPreferences,
    internal val default: T
) {
    /**
     * Key identifier for the setting
     */
    protected abstract val identifier: String

    /**
     * Allow you to set a value for the setting or
     * access a snapshot value of the setting
     */
    abstract var value: T

    /**
     * Readonly observable to listen for changes for the setting
     */
    abstract val flow: Flow<T>
}
