package io.wax911.sample.core.worker

import android.content.Context
import androidx.work.WorkerParameters
import io.wax911.sample.core.presenter.CorePresenter
import io.wax911.support.core.controller.SupportRequestClient
import io.wax911.support.core.worker.SupportWorker
import org.koin.core.KoinComponent
import org.koin.core.inject

class LanguageFetchWorker(
    context: Context,
    workerParameters: WorkerParameters
): SupportWorker<CorePresenter>(context, workerParameters), KoinComponent {

    private val supportRequestClient by inject<SupportRequestClient>()

    override val presenter by inject<CorePresenter>()

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
    override fun doWork(): Result {
        return Result.retry()
    }

}