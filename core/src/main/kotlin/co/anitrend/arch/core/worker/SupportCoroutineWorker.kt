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

package co.anitrend.arch.core.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

/**
 * A [androidx.work.ListenableWorker] implementation that provides interop with Kotlin Coroutines.  Override
 * the [CoroutineWorker.doWork] function to do your suspending work.
 *
 * By default, CoroutineWorker runs on [kotlinx.coroutines.Dispatchers.Default]; this can be modified by wrapping
 * the block of code in [kotlinx.coroutines.withContext] and providing the desired [kotlinx.coroutines.Dispatchers]
 *
 * A CoroutineWorker is given a maximum of ten minutes to finish its execution and return a
 * [androidx.work.ListenableWorker.Result].  After this time has expired, the worker will be signalled to stop.
 *
 * @since v0.9.X
 */
abstract class SupportCoroutineWorker(
    context: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {
    /**
     * A suspending method to do your work.  This function runs on the coroutine context specified
     * by [coroutineContext].
     *
     * A CoroutineWorker is given a maximum of ten minutes to finish its execution and return a
     * [androidx.work.ListenableWorker.Result].  After this time has expired, the worker will be signalled to
     * stop.
     *
     * @return The [androidx.work.ListenableWorker.Result] of the result of the background work; note that
     * dependent work will not execute if you return [androidx.work.ListenableWorker.Result.failure]
     */
    abstract override suspend fun doWork(): Result
}
