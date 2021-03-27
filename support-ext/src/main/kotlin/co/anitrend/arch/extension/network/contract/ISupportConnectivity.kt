package co.anitrend.arch.extension.network.contract

import co.anitrend.arch.extension.network.model.ConnectivityState
import kotlinx.coroutines.flow.Flow

interface ISupportConnectivity {

    /**
     * Check if the device is connected to any network with internet capabilities, this is only
     * a snapshot of the state at the time of request
     *
     * @return true if a internet activity is present otherwise false
     */
    val isConnected: Boolean

    /**
     * Connection state flow, allows us to monitor changes on the network connectivity
     *
     * @see ConnectivityState
     */
    val connectivityStateFlow: Flow<ConnectivityState>
}