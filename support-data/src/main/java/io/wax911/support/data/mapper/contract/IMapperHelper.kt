package io.wax911.support.data.mapper.contract

import io.wax911.support.data.model.NetworkState
import kotlinx.coroutines.Deferred

/**
 *
 * @since v1.1.0
 */
interface IMapperHelper<S> {

    /**
     * Response handler for coroutine contexts which need to observe
     * the live data of [NetworkState]
     *
     * Unless when if using [androidx.paging.PagingRequestHelper.Request.Callback]
     * then you can ignore the return type
     *
     * @param deferred an deferred result awaiting execution
     * @return network state of the deferred result
     */
    suspend fun handleResponse(deferred: Deferred<S>): NetworkState
}