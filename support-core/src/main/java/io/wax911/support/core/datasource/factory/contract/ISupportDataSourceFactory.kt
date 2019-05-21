package io.wax911.support.core.datasource.factory.contract

import androidx.lifecycle.MutableLiveData

interface ISupportDataSourceFactory<S> {

    val sourceLiveData: MutableLiveData<S>
}