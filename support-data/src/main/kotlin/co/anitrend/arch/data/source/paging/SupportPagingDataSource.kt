package co.anitrend.arch.data.source.paging

import co.anitrend.arch.data.request.AbstractRequestHelper
import co.anitrend.arch.data.request.contract.IRequestHelper
import co.anitrend.arch.data.request.extension.createStatusFlow
import co.anitrend.arch.data.request.helper.RequestHelper
import co.anitrend.arch.data.source.paging.contract.AbstractPagingDataSource
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.arch.extension.util.SupportExtKeyStore
import co.anitrend.arch.extension.util.pagination.SupportPagingHelper

/**
 * A data source that is targeted for [androidx.paging.PagedList]
 *
 * @param dispatchers Dispatchers that are currently available
 *
 * @since v1.1.0
 */
abstract class SupportPagingDataSource<T>(
    dispatchers: SupportDispatchers
) : AbstractPagingDataSource<T>(dispatchers) {

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
        pageSize = SupportExtKeyStore.pagingLimit
    )

    /**
     * Invokes [clearDataSource] and should invoke network refresh or reload
     */
    override suspend fun invalidate() {
        clearDataSource(dispatchers.io)
        supportPagingHelper.onPageRefresh()
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
    override suspend fun retry() {
        requestHelper.retryWithStatus(IRequestHelper.Status.SUCCESS)
    }
}