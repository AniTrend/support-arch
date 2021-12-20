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

package co.anitrend.arch.extension.dispatchers.contract

import kotlinx.coroutines.CoroutineDispatcher

/**
 * @property main Context that is confined to the Main thread
 * @property computation Context that is used by all standard builders
 * @property io Context that is designed for offloading blocking IO tasks
 * @property confined Context that is confined to a single thread
 *
 * @since v1.3.0
 */
interface ISupportDispatcher {
    val main: CoroutineDispatcher
    val computation: CoroutineDispatcher
    val io: CoroutineDispatcher
    val confined: CoroutineDispatcher
}
