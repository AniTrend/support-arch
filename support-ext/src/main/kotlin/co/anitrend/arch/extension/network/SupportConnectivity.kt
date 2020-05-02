package co.anitrend.arch.extension.network

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.anitrend.arch.extension.lifecycle.SupportLifecycle

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
 * **Credits**: [ConnectivityChecker](https://github.com/android/plaid/blob/master/core/src/main/java/io/plaidapp/core/ui/ConnectivityChecker.kt)
 *
 * @since v1.2.0
 */
class SupportConnectivity(
    private val connectivityManager: ConnectivityManager?
): SupportLifecycle {

    override val moduleTag: String = SupportConnectivity::class.java.simpleName

    /**
     * Check if the device is connected to any network with internet capabilities
     *
     * @return true if a internet activity is present otherwise false
     */
    val isConnected
        get() = (connectivityManager?.allNetworks?.filter {
            val network = connectivityManager.getNetworkCapabilities(it)
            network?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
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

    /**
     * Triggered when the lifecycleOwner reaches it's onPause state
     *
     * @see [androidx.lifecycle.LifecycleOwner]
     */
    override fun onPause() {
        super.onPause()
        if (monitoringConnectivity) {
            connectivityManager?.unregisterNetworkCallback(connectivityCallback)
            monitoringConnectivity = false
        }
    }

    /**
     * Triggered when the lifecycleOwner reaches it's onResume state
     *
     * @see [androidx.lifecycle.LifecycleOwner]
     */
    override fun onResume() {
        super.onResume()
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