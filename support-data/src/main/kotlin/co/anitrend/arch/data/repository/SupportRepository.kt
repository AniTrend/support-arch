package co.anitrend.arch.data.repository

import co.anitrend.arch.data.repository.contract.ISupportRepository
import kotlinx.coroutines.SupervisorJob

/**
 *
 *
 * @since v1.1.0
 */
abstract class SupportRepository : ISupportRepository {

    protected val moduleTag: String = javaClass.simpleName

    /**
     * Requires an instance of [kotlinx.coroutines.Job] or [kotlinx.coroutines.SupervisorJob]
     */
    override val supervisorJob = SupervisorJob()
}
