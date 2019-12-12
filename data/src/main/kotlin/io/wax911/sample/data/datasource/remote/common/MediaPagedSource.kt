package io.wax911.sample.data.datasource.remote.common

import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import co.anitrend.arch.data.source.paging.SupportPagingDataSource
import co.anitrend.arch.extension.SupportDispatchers

/**
 * For the sake of this sample we will reuse this class
 */
abstract class MediaPagedSource<T>(
    dispatchers: SupportDispatchers
) : SupportPagingDataSource<T>(dispatchers) {

    protected lateinit var executionTarget: (PagingRequestHelper.Request.Callback) -> Unit

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
    override fun onItemAtEndLoaded(itemAtEnd: T) {
        pagingRequestHelper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            supportPagingHelper.onPageNext()
            executionTarget(it)
        }
    }
}