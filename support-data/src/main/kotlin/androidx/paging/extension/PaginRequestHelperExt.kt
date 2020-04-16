package androidx.paging.extension

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingRequestHelper
import co.anitrend.arch.domain.entities.NetworkState

private fun PagingRequestHelper.StatusReport.getErrorMessage(): String {
    return PagingRequestHelper.RequestType.values().mapNotNull {
        getErrorFor(it)?.message
    }.first()
}

/**
 * Creates a live data observable on the paging request helper
 */
fun PagingRequestHelper.createStatusLiveData(): MutableLiveData<NetworkState> {
    val liveData = MutableLiveData<NetworkState>()
    addListener { report ->
        when {
            report.hasRunning() -> liveData.postValue(NetworkState.Loading)
            report.hasError() -> liveData.postValue(
                NetworkState.Error(
                    heading = "Internal paging error",
                    message = report.getErrorMessage()
                )
            )
            else -> liveData.postValue(NetworkState.Success)
        }
    }
    return liveData
}