package io.wax911.sample.core.data.source

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import androidx.paging.toLiveData
import io.wax911.sample.core.api.endpoint.ShowEndpoint
import io.wax911.sample.core.dao.DatabaseHelper
import io.wax911.sample.core.data.mapper.show.AnticipatedShowMapper
import io.wax911.sample.core.data.mapper.show.PopularShowMapper
import io.wax911.sample.core.data.mapper.show.TrendingShowMapper
import io.wax911.sample.core.model.show.Show
import io.wax911.sample.core.repository.show.ShowRequestType
import io.wax911.support.core.data.SupportDataSource
import io.wax911.support.core.util.SupportKeyStore
import kotlinx.coroutines.launch
import org.koin.core.inject
import timber.log.Timber

class ShowDataSource(private val showEndpoint: ShowEndpoint, bundle: Bundle) :
    SupportDataSource<Show>(bundle), SupportDataSource.IDataObservable<PagedList<Show>> {

    override val databaseHelper: DatabaseHelper by inject()

    private fun startRequestForType(
        callback: PagingRequestHelper.Request.Callback
    ): Unit = when (@ShowRequestType val requestType = bundle.getString(SupportKeyStore.arg_request_type)) {
        ShowRequestType.SHOW_TYPE_POPULAR -> {
            showEndpoint.getPopularShows(
                page = supportPagingHelper?.page,
                limit = supportPagingHelper?.pageSize
            ).enqueue(PopularShowMapper(callback).responseCallback)
        }
        ShowRequestType.SHOW_TYPE_TRENDING -> {
            showEndpoint.getTrendingShows(
                page = supportPagingHelper?.page,
                limit = supportPagingHelper?.pageSize
            ).enqueue(TrendingShowMapper(callback).responseCallback)
        }
        ShowRequestType.SHOW_TYPE_ANTICIPATED -> {
            showEndpoint.getAniticipatedShows(
                page = supportPagingHelper?.page,
                limit = supportPagingHelper?.pageSize
            ).enqueue(AnticipatedShowMapper(callback).responseCallback)
        }
        else -> Timber.tag(toString()).e("Unregistered or unknown requestType -> $requestType")
    }

    /**
     * Called when zero items are returned from an initial load of the PagedList's data source.
     */
    override fun onZeroItemsLoaded() {
        pagingRequestHelper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            startRequestForType(it)
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
            supportPagingHelper?.onPageNext()
            startRequestForType(it)
        }
    }

    /**
     * Returns the appropriate observable which we will monitor for updates,
     * common implementation may include but not limited to returning
     * data source live data for a database
     *
     * @param bundle request params, implementation is up to the developer
     */
    override fun observerOnLiveDataWith(bundle: Bundle): LiveData<PagedList<Show>> {
        val showDao = databaseHelper.showDao()
        val requestType = bundle.getString(SupportKeyStore.arg_request_type)

        val dataSourceFactory = when (requestType) {
            ShowRequestType.SHOW_TYPE_POPULAR -> showDao.getPopularItems()
            ShowRequestType.SHOW_TYPE_TRENDING -> showDao.getTrendingItems()
            ShowRequestType.SHOW_TYPE_ANTICIPATED -> showDao.getAnticipatedItems()
            else -> null
        }

        if (dataSourceFactory == null)
            Timber.tag(toString()).e("Unregistered or unknown requestType -> $requestType")

        return dataSourceFactory?.toLiveData(
            config = SupportKeyStore.PAGING_CONFIGURATION,
            boundaryCallback = this,
            initialLoadKey = 1
        ) ?: MutableLiveData()
    }

    override fun refreshOrInvalidate() {
        supportPagingHelper?.onPageRefresh()
        launch {
            databaseHelper.showDao().deleteAll()
        }
    }
}