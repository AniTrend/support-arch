package androidx.paging.extension

import androidx.paging.PagingRequestHelper
import co.anitrend.arch.domain.entities.NetworkState
import kotlinx.coroutines.flow.callbackFlow

private fun PagingRequestHelper.StatusReport.getErrorMessage(): String {
    return PagingRequestHelper.RequestType.values().mapNotNull {
        getErrorFor(it)?.message
    }.first()
}

/**
 * Creates a live data observable on the paging request helper
 */
fun PagingRequestHelper.createStatusLiveData() = callbackFlow {
    val pagingListener = PagingRequestHelper.Listener { report ->
        when {
            report.hasRunning() -> offer(NetworkState.Loading)
            report.hasError() -> offer(
                NetworkState.Error(
                    heading = "Internal paging error",
                    message = report.getErrorMessage()
                )
            )
            else -> offer(NetworkState.Success)
        }
    }
    addListener(pagingListener)
    invokeOnClose {
        removeListener(pagingListener)
    }
}