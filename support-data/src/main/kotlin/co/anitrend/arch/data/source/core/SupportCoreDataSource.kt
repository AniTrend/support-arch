package co.anitrend.arch.data.source.core

import co.anitrend.arch.data.request.contract.IRequestHelper
import co.anitrend.arch.data.source.core.contract.AbstractDataSource
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher

/**
 * A data source that depends on [kotlinx.coroutines.flow.Flow] to publish results.
 *
 * @param dispatcher Dispatchers that are currently available
 *
 * @since v1.1.0
 */
abstract class SupportCoreDataSource(
    dispatcher: ISupportDispatcher
) : AbstractDataSource(dispatcher) {

    /**
     * Invokes [clearDataSource] and should invoke network refresh or reload
     */
    override suspend fun invalidate() {
        clearDataSource(dispatcher.io)
    }

    /**
     * Retries the last executed request, may also be called in [refresh]
     *
     * @see refresh
     */
    override suspend fun retryFailed() {
        requestHelper.retryWithStatus(
            IRequestHelper.Status.FAILED
        ) {}
    }

    /**
     * Invalidate data source and, re-run the last successful or last failed request if applicable
     */
    override suspend fun refresh() {
        val ran = requestHelper.retryWithStatus(
            IRequestHelper.Status.SUCCESS
        ) { invalidate() }

        if (!ran)
            retryFailed()
    }
}