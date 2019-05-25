package io.wax911.support.core.repository

import io.wax911.support.core.factory.contract.IRetrofitFactory
import io.wax911.support.core.repository.contract.ISupportRepository
import io.wax911.support.core.util.SupportConnectivityHelper
import org.koin.core.inject
import timber.log.Timber

abstract class SupportRepository<V>: ISupportRepository<V> {

    protected abstract val retroFactory: IRetrofitFactory

    override val supportConnectivityHelper: SupportConnectivityHelper by inject()

    private fun isConnectedToActiveNetwork(): Boolean {
        return try {
            val connected = supportConnectivityHelper.connectedStatus.value
            connected ?: false
        } catch (e: Exception) {
            Timber.e(e, "Failed to check internet connectivity")
            false
        }
    }
}
