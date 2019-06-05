package io.wax911.support.data.repository

import io.wax911.support.data.factory.contract.IRetrofitFactory
import io.wax911.support.data.repository.contract.ISupportRepository
import kotlinx.coroutines.SupervisorJob

abstract class SupportRepository<V>: ISupportRepository<V> {

    protected val moduleTag: String = javaClass.simpleName

    protected abstract val retroFactory: IRetrofitFactory

    /**
     * Requires an instance of [kotlinx.coroutines.Job] or [kotlinx.coroutines.SupervisorJob]
     */
    override val supervisorJob = SupervisorJob()
}
