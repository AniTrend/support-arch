package io.wax911.sample.core.presenter

import android.content.Context
import androidx.work.*
import io.wax911.sample.core.worker.CountryFetchWorker
import io.wax911.sample.core.worker.GenreFetchWorker
import io.wax911.sample.core.worker.LanguageFetchWorker
import io.wax911.sample.data.util.Settings
import io.wax911.support.core.presenter.SupportPresenter
import java.util.concurrent.TimeUnit

class CorePresenter(
    context: Context,
    settings: Settings
): SupportPresenter<Settings>(context, settings) {

    /**
     * Invokes works to run in the background to fetch genres and tags
     */
    fun syncMetaData() {
        val countryWorker = OneTimeWorkRequest.Builder(CountryFetchWorker::class.java)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 20, TimeUnit.SECONDS)
            .addTag(CountryFetchWorker.TAG)
            .build()

        val genreWorker = OneTimeWorkRequest.Builder(GenreFetchWorker::class.java)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 20, TimeUnit.SECONDS)
            .addTag(GenreFetchWorker.TAG)
            .build()

        val languageWorker = OneTimeWorkRequest.Builder(LanguageFetchWorker::class.java)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 20, TimeUnit.SECONDS)
            .addTag(LanguageFetchWorker.TAG)
            .build()

        WorkManager.getInstance(context)
            .beginUniqueWork(
                META_SYNC_WORKERS,
                ExistingWorkPolicy.KEEP,
                listOf(
                    countryWorker,
                    genreWorker,
                    languageWorker
                )
            ).enqueue()
    }

    companion object {
        private const val META_SYNC_WORKERS = "META_SYNC_WORKERS"
    }
}
