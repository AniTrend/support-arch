package io.wax911.support.data.model.contract

import androidx.lifecycle.LiveData
import io.wax911.support.data.model.NetworkState

interface IUiModel {
    val networkState: LiveData<NetworkState>
    val refreshState: LiveData<NetworkState>
    val refresh: () -> Unit
    val retry: () -> Unit
}