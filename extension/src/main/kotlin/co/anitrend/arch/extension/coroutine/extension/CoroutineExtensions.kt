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

package co.anitrend.arch.extension.coroutine.extension

import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.arch.extension.coroutine.SupportCoroutine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Suppress("FunctionName")
fun Default(): ISupportCoroutine =
    SupportCoroutine(SupervisorJob(), Dispatchers.Default)

@Suppress("FunctionName")
fun Main(): ISupportCoroutine =
    SupportCoroutine(SupervisorJob(), Dispatchers.Main)

@Suppress("FunctionName")
fun Io(): ISupportCoroutine =
    SupportCoroutine(SupervisorJob(), Dispatchers.IO)
