package co.anitrend.arch.data.source.live

import co.anitrend.arch.data.request.AbstractRequestHelper
import co.anitrend.arch.data.request.contract.IRequestHelper
import co.anitrend.arch.data.request.extension.createStatusFlow
import co.anitrend.arch.data.request.helper.RequestHelper
import co.anitrend.arch.data.request.model.Request
import co.anitrend.arch.data.source.live.contract.AbstractPagingLiveDataSource
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.extension.util.DEFAULT_PAGE_SIZE
import co.anitrend.arch.extension.util.pagination.SupportPagingHelper
import kotlinx.coroutines.launch

/**
 * A data source that is targeted for [androidx.paging.PagedList] without a backing source
 *
 * @param dispatcher Dispatchers that are currently available
 *
 * @since v1.3.0
 */
abstract class SupportPagingLiveDataSource<K, V>(
    dispatcher: ISupportDispatcher
) : AbstractPagingLiveDataSource<K, V>(dispatcher) {

    /**
     * Representation of the paging state
     */
    protected open val supportPagingHelper = SupportPagingHelper(
        isPagingLimit = false,
        pageSize = DEFAULT_PAGE_SIZE
    )

    /**
     * Signal the data source to stop loading, and notify its callback.
     *
     * If invalidate has already been called, this method does nothing.
     */
    override fun invalidate() {
        launch { clearDataSource(dispatcher.io) }
        supportPagingHelper.onPageRefresh()
        super.invalidate()
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