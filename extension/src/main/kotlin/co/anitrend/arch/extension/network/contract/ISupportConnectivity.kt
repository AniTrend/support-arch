/**
 * Copyright 2021 AniTrend
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
