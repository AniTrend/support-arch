package io.wax911.support.custom.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.wax911.support.custom.presenter.SupportPresenter

abstract class SupportWorker<P : SupportPresenter<*>>(context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters) {

    protected val presenter by lazy { initPresenter() }

    /**
     * @return the presenter that will be used by the worker
     */
    protected abstract fun initPresenter(): P

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