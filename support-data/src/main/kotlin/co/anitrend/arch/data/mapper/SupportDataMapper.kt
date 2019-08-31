package co.anitrend.arch.data.mapper

import co.anitrend.arch.data.mapper.contract.ISupportDataMapper
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob

/**
 * Provides functionality for mapping objects from one type to another
 *
 * @param parentCoroutineJob parent coroutine from something that is lifecycle aware,
 * this enables us to cancels jobs automatically when the parent is also canceled
 *
 * @since 1.1.0
 */
abstract class SupportDataMapper<S, D>(
    parentCoroutineJob: Job? = null
) : ISupportDataMapper<S, D> {

    protected val moduleTag: String = javaClass.simpleName

    /**
     * Requires an instance of [kotlinx.coroutines.Job] or [kotlinx.coroutines.SupervisorJob]
     */
    override val supervisorJob = SupervisorJob(parentCoroutineJob)
}