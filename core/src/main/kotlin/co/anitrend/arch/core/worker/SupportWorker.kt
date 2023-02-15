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
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * A class that performs work synchronously on a background thread provided by [androidx.work.WorkManager].
 *
 * Worker classes are instantiated at runtime by WorkManager and the [androidx.work.Worker.doWork] method is
 * called on a pre-specified background thread (see [androidx.work.Configuration.getExecutor]).  This
 * method is for **synchronous** processing of your work, meaning that once you return from that
 * method, the Worker is considered to be finished and will be destroyed.  If you need to do your
 * work asynchronously or call asynchronous APIs, you should use [androidx.work.ListenableWorker].
 *
 * In case the work is preempted for any reason, the same instance of Worker is not reused.  This
 * means that [androidx.work.Worker.doWork] is called exactly once per Worker instance.
 * A new Worker is created if a unit of work needs to be rerun.
 *
 * A Worker is given a maximum of ten minutes to finish its execution and return a
 * [androidx.work.ListenableWorker.Result].  After this time has expired, the Worker will be
 * signalled to stop.
 *
 * @since v0.9.X
 */
abstract class SupportWorker(
    context: Context,
    workerParameters: WorkerParameters,
) : Worker(context, workerParameters) {

    /**
     * Override this method to do your actual background processing.  This method is called on a
     * background thread - you are required to **synchronously** do your work and return the
     * [androidx.work.ListenableWorker.Result] from this method.  Once you return from this
     * method, the Worker is considered to have finished what its doing and will be destroyed.  If
     * you need to do your work asynchronously on a thread of your own choice, see
     * [androidx.work.ListenableWorker].
     *
     *
     * A Worker is given a maximum of ten minutes to finish its execution and return a
     * [androidx.work.ListenableWorker.Result].  After this time has expired, the Worker will
     * be signalled to stop.
     *
     * @return The [androidx.work.ListenableWorker.Result] of the computation; note that
     * dependent work will not execute if you use
     * [androidx.work.ListenableWorker.Result.failure] or
     * [androidx.work.ListenableWorker.Result.failure]
     */
    abstract override fun doWork(): Result
}
