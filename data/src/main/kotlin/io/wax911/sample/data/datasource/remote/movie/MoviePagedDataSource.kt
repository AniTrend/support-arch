package io.wax911.sample.data.datasource.remote.movie

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import androidx.paging.toLiveData
import co.anitrend.arch.data.source.contract.ISourceObservable
import co.anitrend.arch.data.util.SupportDataKeyStore
import co.anitrend.arch.extension.SupportDispatchers
import com.uwetrottmann.trakt5.enums.Extended
import com.uwetrottmann.trakt5.services.Movies
import io.wax911.sample.data.datasource.local.query.MovieDao
import io.wax911.sample.data.datasource.remote.movie.contract.TraktMoviePagedSource
import io.wax911.sample.data.entitiy.movie.MovieEntity
import io.wax911.sample.data.mapper.movie.PopularMovieMapper
import io.wax911.sample.data.mapper.movie.TrendingMovieMapper
import kotlinx.coroutines.launch

class MoviePagedDataSource(
    private val movieDao: MovieDao,
    private val movieEndpoint: Movies,
    dispatchers: SupportDispatchers
)  : TraktMoviePagedSource(dispatchers) {

    private fun getPopularMovies(callback: PagingRequestHelper.Request.Callback) {
        val call =
            movieEndpoint.popular(
                supportPagingHelper.page,
                supportPagingHelper.pageSize,
                Extended.FULL
            )

        val mapper = PopularMovieMapper(
            movieDao = movieDao
        )

        launch {
            mapper(call, callback)
        }
    }

    private fun getTrendingMovies(callback: PagingRequestHelper.Request.Callback) {
        val call =
            movieEndpoint.trending(
                supportPagingHelper.page,
                supportPagingHelper.pageSize,
                Extended.FULL
            )

        val mapper = TrendingMovieMapper(
            movieDao = movieDao
        )

        launch {
            mapper(call, callback)
        }
    }

    /**
     * Registers a dispatcher executing a unit of work and then returns a
     * [androidx.lifecycle.LiveData] observable
     *
     * @see MovieDao.getPopularItems
     */
    override val popularMovieObservable =
        object : ISourceObservable<Nothing?, PagedList<MovieEntity>> {
            /**
             * Returns the appropriate observable which we will monitor for updates,
             * common implementation may include but not limited to returning
             * data source live data for a database
             *
             * @param parameter to use when executing
             */
            override fun invoke(parameter: Nothing?): LiveData<PagedList<MovieEntity>> {
                executionTarget = { getPopularMovies(it) }

                val dataSourceFactory = movieDao.getPopularItems()

                return dataSourceFactory.toLiveData(
                    config = SupportDataKeyStore.PAGING_CONFIGURATION,
                    boundaryCallback = this@MoviePagedDataSource
                )
            }
        }

    /**
     * Registers a dispatcher executing a unit of work and then returns a
     * [androidx.lifecycle.LiveData] observable
     *
     * @see MovieDao.getPopularItems
     */
    override val trendingMovieObservable =
        object : ISourceObservable<Nothing?, PagedList<MovieEntity>> {
            /**
             * Returns the appropriate observable which we will monitor for updates,
             * common implementation may include but not limited to returning
             * data source live data for a database
             *
             * @param parameter to use when executing
             */
            override fun invoke(parameter: Nothing?): LiveData<PagedList<MovieEntity>> {
                executionTarget = { getTrendingMovies(it) }

                val dataSourceFactory = movieDao.getTrendingItems()

                return dataSourceFactory.toLiveData(
                    config = SupportDataKeyStore.PAGING_CONFIGURATION,
                    boundaryCallback = this@MoviePagedDataSource
                )
            }
        }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     */
    override suspend fun clearDataSource() {
        movieDao.deleteAll()
    }
}