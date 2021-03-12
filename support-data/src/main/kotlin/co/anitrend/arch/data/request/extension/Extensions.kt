package co.anitrend.arch.data.request.extension

import co.anitrend.arch.data.request.AbstractRequestHelper
import co.anitrend.arch.data.request.report.RequestStatusReport
import co.anitrend.arch.data.request.contract.IRequestHelper
import co.anitrend.arch.domain.entities.RequestError
import co.anitrend.arch.data.request.model.Request
import co.anitrend.arch.domain.entities.LoadState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow

private fun RequestStatusReport.getRequestError(): RequestError {
    return Request.Type.values().mapNotNull {
        getErrorFor(it)
    }.first()
}

private fun RequestStatusReport.getRunningPosition() = when (getType()) {
    Request.Type.INITIAL -> LoadState.Loading.Position.TOP
    Request.Type.BEFORE -> LoadState.Loading.Position.TOP
    Request.Type.AFTER -> LoadState.Loading.Position.BOTTOM
}

/**
 * Creates a live data observable on the paging request helper
 */
internal fun AbstractRequestHelper.createStatusFlow() = callbackFlow<LoadState> {
    val requestListener = object : IRequestHelper.Listener {
        /**
         * Called when the status for any of the requests has changed.
         *
         * @param report The current status report that has all the information about the requests.
         */
        override fun onStatusChange(report: RequestStatusReport) {
            val state = when {
                report.hasRunning() -> LoadState.Loading(report.getRunningPosition())
                report.hasError() -> LoadState.Error(report.getRequestError())
                report.hasSuccess() -> LoadState.Success
                else -> LoadState.Idle
            }
            sendBlocking(state)
        }
    }
    addListener(requestListener)

    awaitClose { removeListener(requestListener) }
}