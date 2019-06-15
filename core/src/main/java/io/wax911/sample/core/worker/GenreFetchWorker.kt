package io.wax911.sample.core.worker

import android.content.Context
import androidx.work.WorkerParameters
import io.wax911.sample.core.presenter.CorePresenter
import io.wax911.sample.data.api.endpoint.MetaEndpoints
import io.wax911.sample.data.source.meta.GenreDataSource
import io.wax911.support.core.worker.SupportCoroutineWorker
import io.wax911.support.data.factory.contract.IRetrofitFactory
import io.wax911.support.data.factory.contract.getEndPointOf
import io.wax911.support.extension.util.SupportConnectivityHelper
import org.koin.core.KoinComponent
import org.koin.core.inject

class GenreFetchWorker(
    context: Context,
    workerParameters: WorkerParameters
) : SupportCoroutineWorker<CorePresenter>(
    context,
    workerParameters
), KoinComponent {

    private val connectivityHelper by inject<SupportConnectivityHelper>()
    private val retroFactory by inject<IRetrofitFactory>()

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
    override suspend fun doWork(): Result {
        if (connectivityHelper.isConnected) {
            val endpoint = retroFactory.getEndPointOf<MetaEndpoints>()
            val mediaTagDataSource = GenreDataSource(endpoint)

            val networkResult = mediaTagDataSource.startRequestForType()

            if (networkResult.isLoaded())
                return Result.success()
            return Result.failure()
        }
        return Result.retry()
    }

    companion object {
        const val TAG = "GenreFetchWorker"
    }
}