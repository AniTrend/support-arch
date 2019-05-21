package io.wax911.sample.core.data.source

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PagingRequestHelper
import io.wax911.sample.core.api.RetroFactory
import io.wax911.sample.core.api.endpoint.ShowEndpoints
import io.wax911.sample.core.model.meta.ResourceType
import io.wax911.sample.core.model.show.Show
import io.wax911.sample.core.repository.show.ShowRequestType
import io.wax911.sample.core.util.StateUtil
import io.wax911.support.core.datasource.SupportPositionalDataSource
import io.wax911.support.core.datasource.factory.SupportDataSourceFactory
import io.wax911.support.core.repository.SupportBoundaryCallback
import io.wax911.support.core.view.model.NetworkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class PopularShowDataSource private constructor(
    bundle: Bundle, private val retroFactory: RetroFactory
) : SupportPositionalDataSource<Show>(bundle) {

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    /**
     * Called to load a range of data from the DataSource.
     *
     *
     * This method is called to load additional pages from the DataSource after the
     * LoadInitialCallback passed to dispatchLoadInitial has initialized a PagedList.
     *
     *
     * Unlike [.loadInitial], this method must return
     * the number of items requested, at the position requested.
     *
     * @param params Parameters for load, including start position and load size.
     * @param callback Callback that receives loaded data.
     */
    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Show>) {
        networkState.postValue(NetworkState.LOADING)
        retroFactory.createService(ShowEndpoints::class.java).getPopularShows(
            page = params.startPosition,
            limit = params.loadSize
        ).enqueue(object : Callback<List<Show>> {
            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected
             * exception occurred creating the request or processing the response.
             */
            override fun onFailure(call: Call<List<Show>>, t: Throwable) {
                retry = {
                    loadRange(params, callback)
                }
                networkState.postValue(NetworkState.error(t.message ?: "unknown error"))
            }

            /**
             * Invoked for a received HTTP response.
             *
             *
             * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * Call [Response.isSuccessful] to determine if the response indicates success.
             */
            override fun onResponse(call: Call<List<Show>>, response: Response<List<Show>>) {
                if (response.isSuccessful) {
                    val data = response.body() ?: emptyList()
                    retry = null
                    callback.onResult(data)
                    networkState.postValue(NetworkState.LOADED)
                } else {
                    retry = {
                        loadRange(params, callback)
                    }
                    networkState.postValue(NetworkState.error("error code: ${response.code()}"))
                }
            }
        })
    }

    /**
     * Load initial list data.
     *
     *
     * This method is called to load the initial page(s) from the DataSource.
     *
     *
     * Result list must be a multiple of pageSize to enable efficient tiling.
     *
     * @param params Parameters for initial load, including requested start position, load size, and
     * page size.
     * @param callback Callback that receives initial load data, including
     * position and total data set size.
     */
    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Show>) {
        val request = retroFactory.createService(ShowEndpoints::class.java).getPopularShows(
            page = 1,
            limit = params.pageSize
        )
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        // triggered by a refresh, we better execute sync
        try {
            val response = request.execute()
            val data = response.body() ?: emptyList()

            retry = null
            networkState.postValue(NetworkState.LOADED)
            initialLoad.postValue(NetworkState.LOADED)
            callback.onResult(data, 0)
        } catch (ioException: IOException) {
            retry = {
                loadInitial(params, callback)
            }
            val error = NetworkState.error(ioException.message ?: "unknown error")
            networkState.postValue(error)
            initialLoad.postValue(error)
        }
    }

    override fun retryAllFailedRequests() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            launch(Dispatchers.Default) {
                it.invoke()
            }
        }
    }

    override fun refreshOrInvalidate() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class Factory(bundle: Bundle) : SupportDataSourceFactory<Int, Show, PopularShowDataSource>(bundle), KoinComponent {

        override val sourceLiveData = MutableLiveData<PopularShowDataSource>()

        private val retroFactory: RetroFactory by inject()

        /**
         * Create a DataSource.
         *
         *
         * The DataSource should invalidate itself if the snapshot is no longer valid. If a
         * DataSource becomes invalid, the only way to query more data is to create a new DataSource
         * from the Factory.
         *
         *
         * [LivePagedListBuilder] for example will construct a new PagedList and DataSource
         * when the current DataSource is invalidated, and pass the new PagedList through the
         * `LiveData<PagedList>` to observers.
         *
         * @return the new DataSource.
         */
        override fun create(): DataSource<Int, Show> {
            val dataSource = PopularShowDataSource(bundle, retroFactory)
            sourceLiveData.postValue(dataSource)
            return dataSource
        }
    }

    class BoundaryCallback(bundle: Bundle, responseHandler: (Bundle, Response<List<Show>?>) -> Unit) :
        SupportBoundaryCallback<Show, List<Show>>(bundle, responseHandler), KoinComponent {

        private val retroFactory: RetroFactory by inject()

        /**
         * Every time it gets new items, boundary callback simply inserts them into the database and
         * paging library takes care of refreshing the list if necessary.
         */
        override fun insertItemsIntoDb(response: Response<List<Show>?>, requestCallback: PagingRequestHelper.Request.Callback) {
            IO_EXECUTOR.execute {
                responseHandler(bundle, response)
                requestCallback.recordSuccess()
            }
        }

        private fun startRequestForType(pagingRequestType: PagingRequestHelper.RequestType, callback: PagingRequestHelper.Request.Callback) {
            val showEndpoints = retroFactory.createService(ShowEndpoints::class.java)
            val page = when (pagingRequestType) {
                PagingRequestHelper.RequestType.INITIAL -> 1
                else -> bundle.getInt(StateUtil.arg_page)
            }
            when (@ShowRequestType val requestType = bundle.getString(StateUtil.arg_request_type)) {
                ShowRequestType.SHOW_TYPE_POPULAR -> {
                    Timber.i("Invoking request for type: $requestType")
                    showEndpoints.getPopularShows(
                        page = page,
                        limit = StateUtil.pagingLimit,
                        resourceType = ResourceType.FULL
                    ).enqueue(createWebserviceCallback(callback))
                }
            }
        }

        /**
         * Called when zero items are returned from an initial load of the PagedList's data source.
         */
        override fun onZeroItemsLoaded() {
            pagingRequestHelper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
                startRequestForType(PagingRequestHelper.RequestType.INITIAL, it)
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
                startRequestForType(PagingRequestHelper.RequestType.AFTER, it)
            }
        }
    }
}