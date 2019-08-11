package io.wax911.sample.core.worker

import android.content.Context
import androidx.work.WorkerParameters
import io.wax911.sample.core.presenter.CorePresenter
import io.wax911.sample.data.model.meta.MediaCategoryContract
import io.wax911.sample.data.usecase.meta.CountryFetchUseCase
import io.wax911.sample.data.usecase.meta.contract.IMetaUseCase
import io.wax911.support.core.worker.SupportCoroutineWorker
import io.wax911.support.extension.util.SupportConnectivityHelper
import org.koin.core.KoinComponent
import org.koin.core.inject

class CountryFetchWorker(
    context: Context, workerParameters: WorkerParameters
) : SupportCoroutineWorker<CorePresenter>(
    context, workerParameters
), KoinComponent {

    private val connectivityHelper by inject<SupportConnectivityHelper>()
    private val useCase: IMetaUseCase by inject<CountryFetchUseCase>()

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
            val showResult = useCase(
                IMetaUseCase.Payload(
                    MediaCategoryContract.SHOWS
                )
            )
            val movieResult = useCase(
                IMetaUseCase.Payload(
                    MediaCategoryContract.SHOWS
                )
            )

            if (showResult.isLoaded() && movieResult.isLoaded())
                return Result.success()
            return Result.failure()
        }
        return Result.retry()
    }

    companion object {
        const val TAG = "io.wax911.sample:CountryFetchWorker"
    }
}