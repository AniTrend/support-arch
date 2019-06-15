package io.wax911.support.data.mapper

import io.wax911.support.data.mapper.contract.ISupportDataMapper
import kotlinx.coroutines.*

/**
 * Provides functionality for mapping objects from one type to another
 *
 * @param parentCoroutineJob parent coroutine from something that is lifecycle aware,
 * this enables us to cancels jobs automatically when the parent is also canceled
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