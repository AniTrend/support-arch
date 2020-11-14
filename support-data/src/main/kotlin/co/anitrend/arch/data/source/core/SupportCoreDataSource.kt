package co.anitrend.arch.data.source.core

import co.anitrend.arch.data.request.AbstractRequestHelper
import co.anitrend.arch.data.request.contract.IRequestHelper
import co.anitrend.arch.data.request.extension.createStatusFlow
import co.anitrend.arch.data.request.helper.RequestHelper
import co.anitrend.arch.data.source.core.contract.AbstractDataSource
import co.anitrend.arch.extension.dispatchers.SupportDispatchers

/**
 * A data source that depends on [kotlinx.coroutines.flow.Flow] to publish results.
 *
 * @param dispatchers Dispatchers that are currently available
 *
 * @since v1.1.0
 */
abstract class SupportCoreDataSource(
    dispatchers: SupportDispatchers
) : AbstractDataSource(dispatchers) {

    /**
     * Request helper that controls the flow of requests to the implementing data source to avoid
     * multiple requests of the same type before others are completed for this instance
     *
     * @see AbstractRequestHelper
     */
    final override val requestHelper = RequestHelper(dispatchers.io)

    /**
     * Observable for network state during requests that the UI can monitor and
     * act based on state changes
     */
    override val networkState = requestHelper.createStatusFlow()


    /**
     * Invokes [clearDataSource] and should invoke network refresh or reload
     */
    override suspend fun invalidate() {
        clearDataSource(dispatchers.io)
    }

    /**
     * Retries the last executed request, may also be called in [refresh]
     *
     * @see refresh
     */
    override suspend fun retryFailed() {
        requestHelper.retryWithStatus()
    }

    /**
     * Invalidate data source and, re-run the last successful or last failed request if applicable
     */
    override suspend fun refresh() {
        invalidate()
        val ran = requestHelper.retryWithStatus(
            IRequestHelper.Status.SUCCESS
        )
        if (!ran)
            retryFailed()
    }
}