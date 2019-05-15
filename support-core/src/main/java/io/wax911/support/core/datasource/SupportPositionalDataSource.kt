package io.wax911.support.core.datasource

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import io.wax911.support.core.datasource.contract.ISupportDataSource
import io.wax911.support.core.view.model.NetworkState
import java.util.concurrent.Executor

abstract class SupportPositionalDataSource<V>(
    private val bundle: Bundle
) : PositionalDataSource<V>(), ISupportDataSource {

    override val networkState = MutableLiveData<NetworkState>()
    override val initialLoad = MutableLiveData<NetworkState>()
}