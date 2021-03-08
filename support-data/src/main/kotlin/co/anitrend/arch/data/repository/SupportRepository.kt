package co.anitrend.arch.data.repository

import co.anitrend.arch.data.repository.contract.ISupportRepository
import co.anitrend.arch.extension.coroutine.ISupportCoroutine

/**
 * Core repository implementation with data source cancellation support
 *
 * @param source Data source that implements coroutine behaviour
 *
 * @since v1.1.0
 */
abstract class SupportRepository(
    private val source: ISupportCoroutine?
) : ISupportRepository {

    /**
     * Module tag for the current context
     */
    protected val moduleTag: String = javaClass.simpleName

    /**
     * Deals with cancellation of any pending or on going operations that the repository
     * might be working on
     */
    override fun onCleared() {
        source?.cancelAllChildren()
    }
}
