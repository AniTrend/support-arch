package co.anitrend.arch.core.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import co.anitrend.arch.core.presenter.SupportPresenter

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
abstract class SupportWorker<P : SupportPresenter<*>>(
    context: Context,
    workerParameters: WorkerParameters
): Worker(context, workerParameters) {

    protected open val presenter: P? = null

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