package io.wax911.support.extension.util

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.*

/**
 * Lifecycle aware connectivity checker that exposes the network connected status via a LiveData.
 *
 * The loss of connectivity while the user scrolls through the feed should NOT be a blocker for the
 * user.
 *
 * The loss of connectivity when the activity is resumed should be a blocker for the user
 * (since we can't get feed items) - in onResume, we should get the connectivity status. If we
 * are NOT connected then we register a listener and wait to be notified. Only once we are
 * connected, we stop listening to connectivity.¬
 *
 * Credits io.plaidapp.core.ui
 */
class SupportConnectivityHelper(
    private val connectivityManager: ConnectivityManager?
): LifecycleObserver {

    /**
     * Check if the device is connected to any network with internet capabilities
     *
     * @return true if a internet activity is present otherwise false
     */
    val isConnected
        get() = (connectivityManager?.allNetworks?.filter {
            val network = connectivityManager.getNetworkCapabilities(it)
            network?.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
            ) ?: false
        }?.size ?: 0) > 0

    private var monitoringConnectivity = false

    private val _connectedStatus = MutableLiveData<Boolean>()
    val connectedStatus: LiveData<Boolean>
        get() = _connectedStatus

    private val connectivityCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _connectedStatus.postValue(true)
            // we are connected, so we can stop listening
            connectivityManager?.unregisterNetworkCallback(this)
            monitoringConnectivity = false
        }

        override fun onLost(network: Network) {
            _connectedStatus.postValue(false)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopMonitoringConnectivity() {
        if (monitoringConnectivity) {
            connectivityManager?.unregisterNetworkCallback(connectivityCallback)
            monitoringConnectivity = false
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startMonitoringConnectivity() {
        connectivityManager?.registerNetworkCallback(
            NetworkRequest.Builder()
                .addCapability(
                    NetworkCapabilities.NET_CAPABILITY_INTERNET
                ).build(),
            connectivityCallback
        )
        monitoringConnectivity = true
    }
}