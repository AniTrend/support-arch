package io.wax911.sample.data.source.meta

import io.wax911.sample.data.api.endpoint.MetaEndpoints
import io.wax911.sample.data.dao.query.LanguageDao
import io.wax911.sample.data.mapper.meta.LanguageMapper
import io.wax911.sample.data.model.meta.MediaCategory
import io.wax911.support.data.model.NetworkState
import io.wax911.support.data.source.coroutine.SupportCoroutineDataSource
import kotlinx.coroutines.Job
import kotlinx.coroutines.async

class LanguageCoroutineDataSource(
    private val metaEndpoints: MetaEndpoints,
    private val languageDao: LanguageDao,
    private val mediaCategory: MediaCategory,
    job: Job? = null
) : SupportCoroutineDataSource(job) {

    /**
     * Handles the requesting data from a the network source and return
     * [NetworkState] to the caller after execution.
     *
     * In this context the super.invoke() method will allow a retry action to be set
     */
    override suspend fun invoke(): NetworkState {
        super.invoke()
        val result = async {
            metaEndpoints.getLanguages(mediaCategory)
        }

        val mapper = LanguageMapper(
            languageDao,
            mediaCategory
        )

        return mapper.handleResponse(result)
    }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     */
    override suspend fun clearDataSource() {
        languageDao.deleteAll()
    }
}