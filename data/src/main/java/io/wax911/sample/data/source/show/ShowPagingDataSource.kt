package io.wax911.sample.data.source.show

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import androidx.paging.toLiveData
import io.wax911.sample.data.api.endpoint.ShowEndpoint
import io.wax911.sample.data.dao.query.ShowDao
import io.wax911.sample.data.mapper.show.AnticipatedShowMapper
import io.wax911.sample.data.mapper.show.PopularShowMapper
import io.wax911.sample.data.mapper.show.TrendingShowMapper
import io.wax911.sample.data.model.show.Show
import io.wax911.sample.data.usecase.media.MediaRequestType
import io.wax911.sample.data.usecase.media.contract.IPagedMediaUseCase
import io.wax911.support.data.mapper.contract.IMapperHelper
import io.wax911.support.data.model.NetworkState
import io.wax911.support.data.source.contract.ISourceObservable
import io.wax911.support.data.source.paging.SupportPagingDataSource
import io.wax911.support.data.util.SupportDataKeyStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber

class ShowPagingDataSource(
    private val showDao: ShowDao,
    private val showEndpoint: ShowEndpoint,
    private val payload: IPagedMediaUseCase.Payload
) : SupportPagingDataSource<Show>() {

    private fun fetchPopularShows(callback: PagingRequestHelper.Request.Callback) {
        val result = async {
            showEndpoint.getPopularShows(
                page = supportPagingHelper.page,
                limit = supportPagingHelper.pageSize
            )
        }

        val mapper = PopularShowMapper(
            showDao = showDao,
            pagingRequestHelper = callback
        )

        launch {
            val state = mapper.handleResponse(result)
            networkState.postValue(state)
        }
    }

    private fun fetchTrendingShows(callback: PagingRequestHelper.Request.Callback) {
        val result = async {
            showEndpoint.getTrendingShows(
                page = supportPagingHelper.page,
                limit = supportPagingHelper.pageSize
            )
        }

        val mapper = TrendingShowMapper(
            showDao = showDao,
            pagingRequestHelper = callback
        )

        launch {
            val state = mapper.handleResponse(result)
            networkState.postValue(state)
        }
    }

    private fun fetchAnticipatedShows(callback: PagingRequestHelper.Request.Callback) {
        val result = async {
            showEndpoint.getAnticipatedShows(
                page = supportPagingHelper.page,
                limit = supportPagingHelper.pageSize
            )
        }

        val mapper = AnticipatedShowMapper(
            showDao = showDao,
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
                fetchPopularShows(callback)
            MediaRequestType.MEDIA_TYPE_TRENDING ->
                fetchTrendingShows(callback)
            MediaRequestType.MEDIA_TYPE_ANTICIPATED ->
                fetchAnticipatedShows(callback)
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
    override fun onItemAtEndLoaded(itemAtEnd: Show) {
        pagingRequestHelper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            supportPagingHelper.onPageNext()
            invoke(it)
        }
    }

    val shows =
        object : ISourceObservable<PagedList<Show>, IPagedMediaUseCase.Payload> {
            /**
             * Returns the appropriate observable which we will monitor for updates,
             * common implementation may include but not limited to returning
             * data source live data for a database
             *
             * @param parameter parameters, implementation is up to the developer
             */
            override operator fun invoke(parameter: IPagedMediaUseCase.Payload): LiveData<PagedList<Show>> {
                val requestType = parameter.requestType

                val dataSourceFactory = when (requestType) {
                    MediaRequestType.MEDIA_TYPE_POPULAR -> showDao.getPopularItems()
                    MediaRequestType.MEDIA_TYPE_TRENDING -> showDao.getTrendingItems()
                    MediaRequestType.MEDIA_TYPE_ANTICIPATED -> showDao.getAnticipatedItems()
                    else -> null
                }

                if (dataSourceFactory == null)
                    Timber.tag(moduleTag).e("Unregistered or unknown requestType -> $requestType")

                return dataSourceFactory?.toLiveData(
                    config = SupportDataKeyStore.PAGING_CONFIGURATION,
                    boundaryCallback = this@ShowPagingDataSource
                ) ?: MutableLiveData()
            }
    }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     */
    override fun clearDataSource() {
        launch {
            showDao.deleteAll()
        }
    }
}
