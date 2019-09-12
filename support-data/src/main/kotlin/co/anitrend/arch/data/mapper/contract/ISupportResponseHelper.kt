package co.anitrend.arch.data.mapper.contract

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingRequestHelper
import co.anitrend.arch.domain.entities.NetworkState
import kotlinx.coroutines.Deferred

/**
 * Contract mapper for wrapping future requests, commonly in the form of a
 * [Deferred] or [retrofit2.Call]
 *
 * @since v1.1.0
 */
interface ISupportResponseHelper<in S> {

    /**
     * Response handler for coroutine contexts, mainly for paging
     *
     * @param resource awaiting execution
     * @param pagingRequestHelper optional paging request callback
     */
    suspend operator fun invoke(
        resource: S,
        pagingRequestHelper: PagingRequestHelper.Request.Callback
    ) {}

    /**
     * Response handler for coroutine contexts which need to observe [NetworkState]
     *
     * @param resource awaiting execution
     * @param networkState for the deferred result
     */
    suspend operator fun invoke(
        resource: S,
        networkState: MutableLiveData<NetworkState>
    ) {}

    /**
     * Response handler for coroutine contexts which need to observe [NetworkState]
     *
     * @param resource awaiting execution
     */
    suspend operator fun invoke(
        resource: S
    ): NetworkState = NetworkState.Loading
}