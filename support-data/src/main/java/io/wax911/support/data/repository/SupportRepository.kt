package io.wax911.support.data.repository

import io.wax911.support.data.repository.contract.ISupportRepository
import kotlinx.coroutines.SupervisorJob

/**
 *
 *
 * @since v1.1.0
 */
abstract class SupportRepository<V, S>: ISupportRepository<V, S> {

    protected val moduleTag: String = javaClass.simpleName

    /**
     * Requires an instance of [kotlinx.coroutines.Job] or [kotlinx.coroutines.SupervisorJob]
     */
    override val supervisorJob = SupervisorJob()
}
