package co.anitrend.arch.data.source.paging

import co.anitrend.arch.data.request.model.Request
import co.anitrend.arch.data.source.paging.contract.AbstractPagingDataSource
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.extension.util.DEFAULT_PAGE_SIZE
import co.anitrend.arch.extension.util.pagination.SupportPagingHelper

/**
 * A data source that is targeted for [androidx.paging.PagedList]
 *
 * @param dispatcher Dispatchers that are currently available
 *
 * @since v1.1.0
 */
abstract class SupportPagingDataSource<T>(
    dispatcher: ISupportDispatcher
) : AbstractPagingDataSource<T>(dispatcher) {

    /**
     * Representation of the paging state
     */
    protected open val supportPagingHelper = SupportPagingHelper(
        isPagingLimit = false,
        pageSize = DEFAULT_PAGE_SIZE
    )

    /**
     * Invokes [clearDataSource] and should invoke network refresh or reload
     */
    override suspend fun invalidate() {
        clearDataSource(dispatcher.io)
        supportPagingHelper.onPageRefresh()
    }

    /**
     * Performs the necessary operation to invoke a network retry request
     */
    override suspend fun retryFailed() {
        requestHelper.retryWithStatus(
            Request.Status.FAILED
        ) {}
    }

    /**
     * Invalidate data source and, re-run the last successful or last failed request if applicable
     */
    override suspend fun refresh() {
        invalidate()
    }
}