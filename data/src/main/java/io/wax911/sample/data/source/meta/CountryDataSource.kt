package io.wax911.sample.data.source.meta

import android.os.Bundle
import io.wax911.sample.data.api.endpoint.MetaEndpoints
import io.wax911.sample.data.arch.source.WorkerDataSource
import io.wax911.sample.data.dao.DatabaseHelper
import io.wax911.sample.data.mapper.meta.CountryMapper
import io.wax911.sample.data.model.meta.MediaCategory
import io.wax911.sample.data.model.meta.MediaCategoryContract
import io.wax911.support.data.model.NetworkState
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import org.koin.core.inject

class CountryDataSource(
    private val metaEndpoints: MetaEndpoints,
    job: Job? = null
) : WorkerDataSource(job) {

    override val databaseHelper by inject<DatabaseHelper>()

    /**
     * Handles the requesting data from a the network source and informs the
     * network state that it is in the loading state
     *
     * @param bundle request parameters or more
     */
    override suspend fun startRequestForType(bundle: Bundle?): NetworkState {
        val mediaCategory: MediaCategory = bundle?.getString(
            MediaCategoryContract.MEDIA_CATEGORY_TYPE
        ) ?: MediaCategoryContract.SHOWS

        val result = async {
            metaEndpoints.getCountries(mediaCategory)
        }

        val mapper = CountryMapper(
            databaseHelper.countryDao(),
            mediaCategory
        )

        return mapper.handleResponse(result)
    }

    /**
     * Clears all the data in a database table which will assure that
     * and refresh the backing storage medium with new network data
     *
     * @param bundle the request request parameters to use
     */
    override suspend fun refreshOrInvalidate(bundle: Bundle?): NetworkState {
        databaseHelper.countryDao().deleteAll()
        return startRequestForType(bundle)
    }
}