package io.wax911.support.core.datasource

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import io.wax911.support.core.datasource.contract.ISupportDataSource
import io.wax911.support.core.view.model.NetworkState
import java.util.concurrent.Executor

abstract class SupportKeyDataSource<K, V>(
    private val bundle: Bundle
) : ItemKeyedDataSource<K, V>(), ISupportDataSource {

    override val networkState = MutableLiveData<NetworkState>()
    override val initialLoad = MutableLiveData<NetworkState>()
}