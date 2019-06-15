package io.wax911.sample.data.source

import android.os.Bundle
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
import io.wax911.sample.data.repository.show.ShowRequestType
import io.wax911.support.data.source.paging.SupportPagingDataSource
import io.wax911.support.data.source.contract.ISourceObservable
import io.wax911.support.data.util.SupportDataKeyStore
import io.wax911.support.extension.util.SupportExtKeyStore
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber

class ShowPagingDataSource(
    private val showDao: ShowDao,
    private val showEndpoint: ShowEndpoint,
    private val bundle: Bundle
) : SupportPagingDataSource<Show>() {

    /**
     * Dispatches work for the paging data source to respective workers or mappers
     * that publish the result to any [androidx.lifecycle.LiveData] observers
     *
     * @see networkState
     */
    override fun invoke(callback: PagingRequestHelper.Request.Callback) {
        when (@ShowRequestType val requestType = bundle.getString(SupportExtKeyStore.arg_request_type)) {
            ShowRequestType.SHOW_TYPE_POPULAR -> {
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
                    mapper.handleResponse(result)
                }
            }
            ShowRequestType.SHOW_TYPE_TRENDING -> {
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
                    mapper.handleResponse(result)
                }
            }
            ShowRequestType.SHOW_TYPE_ANTICIPATED -> {
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
                    mapper.handleResponse(result)
                }
            }
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

    val shows = object : ISourceObservable<PagedList<Show>, Bundle> {

        /**
         * Returns the appropriate observable which we will monitor for updates,
         * common implementation may include but not limited to returning
         * data source live data for a database
         *
         * @param parameter parameters, implementation is up to the developer
         */
        override operator fun invoke(parameter: Bundle): LiveData<PagedList<Show>> {
            val requestType = bundle.getString(SupportExtKeyStore.arg_request_type)

            val dataSourceFactory = when (requestType) {
                ShowRequestType.SHOW_TYPE_POPULAR -> showDao.getPopularItems()
                ShowRequestType.SHOW_TYPE_TRENDING -> showDao.getTrendingItems()
                ShowRequestType.SHOW_TYPE_ANTICIPATED -> showDao.getAnticipatedItems()
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