package io.wax911.sample.data.source.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import androidx.paging.toLiveData
import io.wax911.sample.data.api.endpoint.MovieEndpoint
import io.wax911.sample.data.dao.query.MovieDao
import io.wax911.sample.data.mapper.movie.AnticipatedMovieMapper
import io.wax911.sample.data.mapper.movie.PopularMovieMapper
import io.wax911.sample.data.mapper.movie.TrendingMovieMapper
import io.wax911.sample.data.model.movie.Movie
import io.wax911.sample.data.usecase.media.MediaRequestType
import io.wax911.sample.data.usecase.media.contract.IPagedMediaUseCase
import io.wax911.support.data.source.contract.ISourceObservable
import io.wax911.support.data.source.paging.SupportPagingDataSource
import io.wax911.support.data.util.SupportDataKeyStore
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber

class MoviePagingDataSource(
    private val movieDao: MovieDao,
    private val movieEndpoint: MovieEndpoint,
    private val payload: IPagedMediaUseCase.Payload
) : SupportPagingDataSource<Movie>() {

    private fun fetchPopularMovies(callback: PagingRequestHelper.Request.Callback) {
        val result = async {
            movieEndpoint.getPopularMovies(
                page = supportPagingHelper.page,
                limit = supportPagingHelper.pageSize
            )
        }

        val mapper = PopularMovieMapper(
            movieDao = movieDao,
            pagingRequestHelper = callback
        )

        launch {
            val state = mapper.handleResponse(result)
            networkState.postValue(state)
        }
    }

    private fun fetchTrendingMovies(callback: PagingRequestHelper.Request.Callback) {
        val result = async {
            movieEndpoint.getTrendingMovies(
                page = supportPagingHelper.page,
                limit = supportPagingHelper.pageSize
            )
        }

        val mapper = TrendingMovieMapper(
            movieDao = movieDao,
            pagingRequestHelper = callback
        )

        launch {
            val state = mapper.handleResponse(result)
            networkState.postValue(state)
        }
    }

    private fun fetchAnticipatedMovies(callback: PagingRequestHelper.Request.Callback) {
        val result = async {
            movieEndpoint.getAniticipatedMovies(
                page = supportPagingHelper.page,
                limit = supportPagingHelper.pageSize
            )
        }

        val mapper = AnticipatedMovieMapper(
            movieDao = movieDao,
            pagingRequestHelper = callback
        )

        launch {
            val state = mapper.handleResponse(result)
            networkState.postValue(state)
        }
    }

    /**
     * Dispatches work for the paging data source to respective workers or mappers
     * that publish the result to any [androidx.lifecycle.LiveData] observers
     *
     * @see networkState
     */
    override fun invoke(callback: PagingRequestHelper.Request.Callback) {
        when (val requestType = payload.requestType) {
            MediaRequestType.MEDIA_TYPE_POPULAR ->
                fetchPopularMovies(callback)
            MediaRequestType.MEDIA_TYPE_TRENDING ->
                fetchTrendingMovies(callback)
            MediaRequestType.MEDIA_TYPE_ANTICIPATED ->
                fetchAnticipatedMovies(callback)
            else -> Timber.tag(moduleTag).e("Unregistered or unknown requestType -> $requestType")
        }
    }

    /**
     * Called when zero items are returned from an initial load of the PagedList's data source.
     */
    override fun onZeroItemsLoaded() {
        pagingRequestHelper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            invoke(it)
        }
    }

    /**
     * Called when the item at the end of the PagedList has been loaded, and access has
     * occurred within [Config.prefetchDistance] of it.
     *
     *
     * No more data will be appended to the PagedList after this item.
     *
     * @param itemAtEnd The first item of PagedList
     */
    override fun onItemAtEndLoaded(itemAtEnd: Movie) {
        pagingRequestHelper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            supportPagingHelper.onPageNext()
            invoke(it)
        }
    }

    val movies =
        object : ISourceObservable<PagedList<Movie>, IPagedMediaUseCase.Payload> {
            /**
             * Returns the appropriate observable which we will monitor for updates,
             * common implementation may include but not limited to returning
             * data source live data for a database
             *
             * @param parameter parameters, implementation is up to the developer
             */
            override operator fun invoke(parameter: IPagedMediaUseCase.Payload): LiveData<PagedList<Movie>> {
                val requestType = parameter.requestType

                val dataSourceFactory = when (requestType) {
                    MediaRequestType.MEDIA_TYPE_POPULAR -> movieDao.getPopularItems()
                    MediaRequestType.MEDIA_TYPE_TRENDING -> movieDao.getTrendingItems()
                    MediaRequestType.MEDIA_TYPE_ANTICIPATED -> movieDao.getAnticipatedItems()
                    else -> null
                }

                if (dataSourceFactory == null)
                    Timber.tag(moduleTag).e("Unregistered or unknown requestType -> $requestType")

                return dataSourceFactory?.toLiveData(
                    config = SupportDataKeyStore.PAGING_CONFIGURATION,
                    boundaryCallback = this@MoviePagingDataSource
                ) ?: MutableLiveData()
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