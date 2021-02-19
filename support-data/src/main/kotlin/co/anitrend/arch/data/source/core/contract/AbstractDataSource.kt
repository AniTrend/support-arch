package co.anitrend.arch.data.source.core.contract

import co.anitrend.arch.data.request.AbstractRequestHelper
import co.anitrend.arch.data.request.extension.createStatusFlow
import co.anitrend.arch.data.request.helper.RequestHelper
import co.anitrend.arch.data.source.contract.IDataSource
import co.anitrend.arch.data.source.contract.ISource
import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.arch.extension.coroutine.extension.Default
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher

/**
 * Contract for data sources
 *
 * @since v1.1.0
 */
abstract class AbstractDataSource : IDataSource, ISource, ISupportCoroutine by Default() {

    /**
     * Module tag for the current context
     */
    protected val moduleTag: String = javaClass.simpleName

    /**
     * Contract for multiple types of [coroutineDispatcher]
     */
    protected abstract val dispatcher: ISupportDispatcher

    /**
     * Request helper that controls the flow of requests to the implementing data source to avoid
     * multiple requests of the same type before others are completed for this instance
     *
     * @see AbstractRequestHelper
     */
    override val requestHelper by lazy {
        RequestHelper(dispatcher)
    }

    /**
     * Observable for network state during requests that the UI can monitor and
     * act based on state changes
     */
    override val networkState by lazy {
        requestHelper.createStatusFlow()
    }
}