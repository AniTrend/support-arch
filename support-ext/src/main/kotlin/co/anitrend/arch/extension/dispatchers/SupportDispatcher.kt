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

package co.anitrend.arch.extension.dispatchers

import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext

/**
 * @param main Context that is confined to the Main thread operating with UI objects
 * @param computation Context that is used by all standard builders, It is backed by a
 * shared pool of threads on JVM. By default, the maximal level of parallelism used
 * by this dispatcher is equal to the number of CPU cores, but is at least two.
 * @param io Context that is designed for offloading blocking IO tasks to a shared pool of threads.
 * @param confined Context that is confined to a single thread
 *
 * @since v1.2.0
 */
data class SupportDispatcher(
    override val main: CoroutineDispatcher = Dispatchers.Main,
    override val computation: CoroutineDispatcher = Dispatchers.Default,
    override val io: CoroutineDispatcher = Dispatchers.IO,
    @OptIn(ObsoleteCoroutinesApi::class)
    override val confined: CoroutineDispatcher = newSingleThreadContext("ConfinedContext")
) : ISupportDispatcher
