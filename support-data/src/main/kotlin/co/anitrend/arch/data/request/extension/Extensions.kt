package co.anitrend.arch.data.request.extension

import co.anitrend.arch.data.request.AbstractRequestHelper
import co.anitrend.arch.data.request.report.RequestStatusReport
import co.anitrend.arch.data.request.contract.IRequestHelper
import co.anitrend.arch.data.request.error.RequestError
import co.anitrend.arch.domain.entities.NetworkState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow

private fun RequestStatusReport.getRequestError(): RequestError {
    return IRequestHelper.RequestType.values().mapNotNull {
        getErrorFor(it)
    }.first()
}

/**
 * Creates a live data observable on the paging request helper
 */
internal fun AbstractRequestHelper.createStatusFlow() = callbackFlow<NetworkState> {
    val requestListener = object : IRequestHelper.Listener {
        /**
         * Called when the status for any of the requests has changed.
         *
         * @param report The current status report that has all the information about the requests.
         */
        override fun onStatusChange(report: RequestStatusReport) {
            val state = when {
                report.hasRunning() -> NetworkState.Loading()
                report.hasError() -> {
                    val error = report.getRequestError()
                    NetworkState.Error(
                        heading = error.topic,
                        message = error.description
                    )
                }
                else -> NetworkState.Idle
            }
            sendBlocking(state)
        }
    }
    addListener(requestListener)

    awaitClose { removeListener(requestListener) }
}