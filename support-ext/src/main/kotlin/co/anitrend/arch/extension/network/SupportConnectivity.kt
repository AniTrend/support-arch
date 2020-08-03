package co.anitrend.arch.extension.network

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import co.anitrend.arch.extension.lifecycle.SupportLifecycle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.properties.ReadOnlyProperty

/**
 * Inspired by [ConnectivityChecker](https://github.com/android/plaid/blob/master/core/src/main/java/io/plaidapp/core/ui/ConnectivityChecker.kt)
 *
 * Lifecycle aware connectivity checker that exposes the network connected status via a [StateFlow].
 *
 * The loss of connectivity when the activity is resumed should be a blocker for the user
 * in [onResume], we should get the connectivity status.
 *
 * @since v1.2.0
 */
open class SupportConnectivity(
    private val connectivityManager: ConnectivityManager?
): SupportLifecycle {

    override val moduleTag: String = SupportConnectivity::class.java.simpleName

    /**
     * Check if the device is connected to any network with internet capabilities, this is only
     * a snapshot of the state at the time of request
     *
     * @return true if a internet activity is present otherwise false
     */
    open val isConnected
        get() = (connectivityManager?.allNetworks?.filter {
            val network = connectivityManager.getNetworkCapabilities(it)
            network?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
        }?.size ?: 0) > 0

    @ExperimentalCoroutinesApi
    private val connectedMutableStateFlow =
        MutableStateFlow<ConnectivityState>(ConnectivityState.Unknown)

    /**
     * Connection state flow, allows us to monitor changes on the network connectivity
     *
     * @see ConnectivityState
     */
    @ExperimentalCoroutinesApi
    val connectivityState: StateFlow<ConnectivityState> = connectedMutableStateFlow

    @ExperimentalCoroutinesApi
    private val connectivityCallback =
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                connectedMutableStateFlow.value = ConnectivityState.Connected
            }

            override fun onLost(network: Network) {
                connectedMutableStateFlow.value = ConnectivityState.Disconnected
            }
        }

    /**
     * Triggered when the lifecycleOwner reaches it's onPause state
     *
     * @see [androidx.lifecycle.LifecycleOwner]
     */
    @ExperimentalCoroutinesApi
    override fun onPause() {
        super.onPause()
        connectivityManager?.unregisterNetworkCallback(connectivityCallback)
    }

    /**
     * Triggered when the lifecycleOwner reaches it's onResume state
     *
     * @see [androidx.lifecycle.LifecycleOwner]
     */
    @ExperimentalCoroutinesApi
    override fun onResume() {
        super.onResume()
        connectivityManager?.registerNetworkCallback(
            NetworkRequest.Builder()
                .addCapability(
                    NetworkCapabilities.NET_CAPABILITY_INTERNET
                ).build(),
            connectivityCallback
        )
    }
}