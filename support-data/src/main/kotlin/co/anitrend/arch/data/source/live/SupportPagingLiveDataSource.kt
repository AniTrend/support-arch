package co.anitrend.arch.data.source.live

import co.anitrend.arch.data.request.AbstractRequestHelper
import co.anitrend.arch.data.request.extension.createStatusFlow
import co.anitrend.arch.data.request.helper.RequestHelper
import co.anitrend.arch.data.source.live.contract.AbstractPagingLiveDataSource
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.arch.extension.util.DEFAULT_PAGE_SIZE
import co.anitrend.arch.extension.util.pagination.SupportPagingHelper
import kotlinx.coroutines.launch

/**
 * A data source that is targeted for [androidx.paging.PagedList] without a backing source
 *
 * @param dispatchers Dispatchers that are currently available
 *
 * @since v1.3.0
 */
abstract class SupportPagingLiveDataSource<K, V>(
    dispatchers: SupportDispatchers
) : AbstractPagingLiveDataSource<K, V>(dispatchers) {

    /**
     * Request helper that controls the flow of requests to the implementing data source to avoid
     * multiple requests of the same type before others are completed for this instance
     *
     * @see AbstractRequestHelper
     */
    override val requestHelper = RequestHelper(dispatchers.io)

    /**
     * Observable for network state during requests that the UI can monitor and
     * act based on state changes
     */
    override val networkState by lazy {
        requestHelper.createStatusFlow()
    }

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
        launch { clearDataSource(dispatchers.io) }
        supportPagingHelper.onPageRefresh()
        super.invalidate()
    }

    /**
     * Performs the necessary operation to invoke a network retry request
     */
    override suspend fun retryFailed() {
        requestHelper.retryWithStatus()
    }

    /**
     * Re-run the last successful request if applicable
     */
    override suspend fun refresh() {
        invalidate()
    }
}