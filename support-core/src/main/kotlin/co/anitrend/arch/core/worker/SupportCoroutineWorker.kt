package co.anitrend.arch.core.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import co.anitrend.arch.core.presenter.SupportPresenter

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
    workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {

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