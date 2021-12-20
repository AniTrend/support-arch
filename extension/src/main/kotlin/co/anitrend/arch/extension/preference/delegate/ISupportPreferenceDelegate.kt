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

package co.anitrend.arch.extension.preference.delegate

import co.anitrend.arch.extension.settings.contract.AbstractSetting
import kotlin.properties.ReadWriteProperty

/**
 * Helper interface used for implementing property delegates of read-write properties in settings.
 *
 * @property key lookup key for the preference
 * @property default default value to return
 *
 * @param T the type of the property value.
 *
 * @since v1.3.0
 */
internal interface ISupportPreferenceDelegate<T> : ReadWriteProperty<AbstractSetting<T>, T> {
    val key: String
    val default: T
}
