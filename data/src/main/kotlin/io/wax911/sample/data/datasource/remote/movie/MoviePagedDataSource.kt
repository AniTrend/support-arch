package io.wax911.sample.data.datasource.remote.movie

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import androidx.paging.toLiveData
import co.anitrend.arch.data.source.contract.ISourceObservable
import co.anitrend.arch.data.util.SupportDataKeyStore
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
    private val movieEndpoint: Movies
)  : TraktMoviePagedSource() {

    private lateinit var executionTarget: (PagingRequestHelper.Request.Callback) -> Unit

    private fun getPopularMovies(callback: PagingRequestHelper.Request.Callback) {
        val call =
            movieEndpoint.popular(
                supportPagingHelper.page,
                supportPagingHelper.pageSize,
                Extended.FULL
            )

        val mapper = PopularMovieMapper(
            movieDao = movieDao,
            pagingRequestHelper = callback
        )

        launch {
            val state = mapper(call)
            networkState.postValue(state)
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
            movieDao = movieDao,
            pagingRequestHelper = callback
        )

        launch {
            val state = mapper(call)
            networkState.postValue(state)
        }
    }

    /**
     * Called when zero items are returned from an initial load of the PagedList's data source.
     */
    override fun onZeroItemsLoaded() {
        pagingRequestHelper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            executionTarget(it)
        }
    }

    /**
     * Called when the item at the end of the PagedList has been loaded, and access has
     * occurred within [PagedList.Config.prefetchDistance] of it.
     *
     *
     * No more data will be appended to the PagedList after this item.
     *
     * @param itemAtEnd The first item of PagedList
     */
    override fun onItemAtEndLoaded(itemAtEnd: MovieEntity) {
        pagingRequestHelper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            supportPagingHelper.onPageNext()
            executionTarget(it)
        }
    }

    /**
     * Registers a dispatcher executing a unit of work and then returns a
     * [androidx.lifecycle.LiveData] observable
     *
     * @see MovieDao.getPopularItems
     */
    override val popularMovieObservable =
        object : ISourceObservable<PagedList<MovieEntity>, Nothing?> {
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
        object : ISourceObservable<PagedList<MovieEntity>, Nothing?> {
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
    override fun clearDataSource() {
        launch {
            movieDao.deleteAll()
        }
    }
}