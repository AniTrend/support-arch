package co.anitrend.arch.data.source.core.contract

import co.anitrend.arch.data.source.contract.IDataSource
import co.anitrend.arch.data.source.contract.ISource
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob

/**
 * Contract for data sources
 *
 * @since v1.1.0
 */
abstract class AbstractDataSource(
    protected val dispatchers: SupportDispatchers
) : IDataSource, ISource {

    /**
     * Module tag for the current context
     */
    protected val moduleTag: String = javaClass.simpleName

    /**
     * Requires an instance of [kotlinx.coroutines.Job] or [kotlinx.coroutines.SupervisorJob]
     */
    final override val supervisorJob: Job = SupervisorJob()

    /**
     * Coroutine dispatcher specification
     *
     * @return one of the sub-types of [kotlinx.coroutines.Dispatchers]
     */
    final override val coroutineDispatcher = dispatchers.computation

    /**
     * Persistent context for the coroutine
     *
     * @return [kotlin.coroutines.CoroutineContext] preferably built from
     * [supervisorJob] + [coroutineDispatcher]
     */
    final override val coroutineContext = supervisorJob + coroutineDispatcher

    /**
     * A failure or cancellation of a child does not cause the supervisor job
     * to fail and does not affect its other children.
     *
     * @return [kotlinx.coroutines.CoroutineScope]
     */
    final override val scope = CoroutineScope(coroutineContext)
}