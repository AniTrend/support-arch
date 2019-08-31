package androidx.paging.extension

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingRequestHelper
import co.anitrend.arch.domain.entities.NetworkState

private fun getErrorMessage(report: PagingRequestHelper.StatusReport): String {
    return PagingRequestHelper.RequestType.values().mapNotNull {
        report.getErrorFor(it)?.message
    }.first()
}

fun PagingRequestHelper.createStatusLiveData(): MutableLiveData<NetworkState> {
    val liveData = MutableLiveData<NetworkState>()
    addListener { report ->
        when {
            report.hasRunning() -> liveData.postValue(NetworkState.Loading)
            report.hasError() -> liveData.postValue(
                NetworkState.Error(message = getErrorMessage(report))
            )
            else -> liveData.postValue(NetworkState.Success)
        }
    }
    return liveData
}