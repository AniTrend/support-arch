package io.wax911.support.core.datasource.factory

import android.os.Bundle
import androidx.paging.DataSource
import io.wax911.support.core.datasource.factory.contract.ISupportDataSourceFactory

abstract class SupportDataSourceFactory<K, V, S>(
    protected val bundle: Bundle
) : DataSource.Factory<K, V>(), ISupportDataSourceFactory<S>