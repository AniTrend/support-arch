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

package co.anitrend.arch.data.request.listener

import co.anitrend.arch.data.request.contract.IRequestHelper
import co.anitrend.arch.data.request.extension.getRequestError
import co.anitrend.arch.data.request.extension.getTypeLoadPosition
import co.anitrend.arch.data.request.report.contract.IRequestStatusReport
import co.anitrend.arch.domain.entities.LoadState
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.onFailure
import timber.log.Timber

/**
 * Request helper listener that handles status report changes
 *
 * @param scope Coroutine scope expected to be updated
 *
 * @since 1.3.0
 * @see IRequestStatusReport
 */
internal class RequestHelperListener(
    private val scope: ProducerScope<LoadState>
) : IRequestHelper.Listener {

    /**
     * Called when the status for any of the requests has changed.
     *
     * @param report The current status report that has all the information about the requests.
     */
    override fun onStatusChange(report: IRequestStatusReport) {
        val position = report.getTypeLoadPosition()
        Timber.d("Updating state for -> type: ${report.getType()} | position: $position")
        val state = when {
            report.hasRunning() -> {
                LoadState.Loading(position)
            }
            report.hasError() -> {
                val error = report.getRequestError()
                LoadState.Error(error, position)
            }
            report.hasSuccess() -> {
                LoadState.Success(position)
            }
            else -> LoadState.Idle(position)
        }
        scope.trySend(state).onFailure {
            Timber.e(it, "Failed to send status change report state to scope")
        }
    }
}
