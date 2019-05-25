package io.wax911.support.core.data.contract

import io.wax911.support.core.util.SupportCoroutineHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinComponent

interface ISupportDataSource : KoinComponent, SupportCoroutineHelper {

    val TAG
        get() = javaClass.simpleName

    /**
     * Should clear all the data in a database table which will assure that
     * [androidx.paging.PagingRequestHelper.RequestType.INITIAL] gets triggered
     */
    fun refreshOrInvalidate()

    /**
     * Coroutine dispatcher specification
     *
     * @return [kotlinx.coroutines.Dispatchers.Default] by default
     */
    override val coroutineDispatcher: CoroutineDispatcher
        get() =  Dispatchers.IO
}