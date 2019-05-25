package io.wax911.support.core.repository

import android.content.Context
import android.net.ConnectivityManager
import io.wax911.support.core.factory.contract.IRetrofitFactory
import io.wax911.support.core.repository.contract.ISupportRepository
import timber.log.Timber

abstract class SupportRepository<V>(context: Context): ISupportRepository<V> {

    protected abstract val retroFactory: IRetrofitFactory

    override val connectivityManager: ConnectivityManager? = context.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager?

    private fun isConnectedToActiveNetwork(): Boolean {
        return try {
            val connected = connectivityManager?.activeNetworkInfo?.isConnected
            connected ?: false
        } catch (e: Exception) {
            Timber.e(e, "Failed to check internet connectivity")
            false
        }
    }
}
