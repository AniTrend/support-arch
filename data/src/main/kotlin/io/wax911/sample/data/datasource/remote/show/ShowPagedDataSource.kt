package io.wax911.sample.data.datasource.remote.show

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import androidx.paging.toLiveData
import co.anitrend.arch.data.source.contract.ISourceObservable
import co.anitrend.arch.data.util.SupportDataKeyStore
import co.anitrend.arch.extension.SupportDispatchers
import com.uwetrottmann.trakt5.enums.Extended
import com.uwetrottmann.trakt5.services.Shows
import io.wax911.sample.data.datasource.local.query.ShowDao
import io.wax911.sample.data.datasource.remote.show.contract.TraktShowPagedSource
import io.wax911.sample.data.entitiy.show.ShowEntity
import io.wax911.sample.data.mapper.show.PopularShowMapper
import io.wax911.sample.data.mapper.show.TrendingShowMapper
import kotlinx.coroutines.launch

class ShowPagedDataSource(
    private val showDao: ShowDao,
    private val showEndpoint: Shows,
    dispatchers: SupportDispatchers
) : TraktShowPagedSource(dispatchers) {

    private fun getPopularShows(callback: PagingRequestHelper.Request.Callback) {
        val call = showEndpoint.popular(
            supportPagingHelper.page,
            supportPagingHelper.pageSize,
            Extended.FULL
        )

        val mapper = PopularShowMapper(
            showDao = showDao
        )

        launch {
            mapper(call, callback)
        }
    }

    private fun getTrendingShows(callback: PagingRequestHelper.Request.Callback) {
        val call = showEndpoint.trending(
            supportPagingHelper.page,
            supportPagingHelper.pageSize,
            Extended.FULL
        )

        val mapper = TrendingShowMapper(
            showDao = showDao
        )

        launch {
            mapper(call, callback)
        }
    }

    /**
     * Registers a dispatcher executing a unit of work and then returns a
     * [androidx.lifecycle.LiveData] observable
     *
     * @see ShowDao.getPopularItems
     */
    override val popularShowObservable =
        object : ISourceObservable<Nothing?, PagedList<ShowEntity>> {
            /**
             * Returns the appropriate observable which we will monitor for updates,
             * common implementation may include but not limited to returning
             * data source live data for a database
             *
             * @param parameter parameters, implementation is up to the developer
             */
            override operator fun invoke(parameter: Nothing?): LiveData<PagedList<ShowEntity>> {
                executionTarget = { getPopularShows(it) }
                val dataSourceFactory = showDao.getPopularItems()

                return dataSourceFactory.toLiveData(
                    config = SupportDataKeyStore.PAGING_CONFIGURATION,
                    boundaryCallback = this@ShowPagedDataSource
                )
            }
    }

    /**
     * Registers a dispatcher executing a unit of work and then returns a
     * [androidx.lifecycle.LiveData] observable
     *
     * @see ShowDao.getPopularItems
     */
    override val trendingShowObservable =
        object : ISourceObservable<Nothing?, PagedList<ShowEntity>> {
            /**
             * Returns the appropriate observable which we will monitor for updates,
             * common implementation may include but not limited to returning
             * data source live data for a database
             *
             * @param parameter parameters, implementation is up to the developer
             */
            override operator fun invoke(parameter: Nothing?): LiveData<PagedList<ShowEntity>> {
                executionTarget = { getTrendingShows(it) }

                val dataSourceFactory = showDao.getTrendingItems()

                return dataSourceFactory.toLiveData(
                    config = SupportDataKeyStore.PAGING_CONFIGURATION,
                    boundaryCallback = this@ShowPagedDataSource
                )
            }
    }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     */
    override suspend fun clearDataSource() {
        showDao.deleteAll()
    }
}
