package io.wax911.sample.data.arch.mapper

import androidx.paging.PagingRequestHelper
import io.wax911.support.data.mapper.SupportDataMapper
import io.wax911.support.data.mapper.contract.IMapperHelper
import io.wax911.support.data.model.NetworkState
import io.wax911.support.data.model.contract.SupportStateContract
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import retrofit2.Response

/**
 * TraktTrendMapper specific mapper, extends and overrides the [handleResponse] callback
 * this makes it easier for us to implement error logging and provide better error messages
 *
 * @see SupportDataMapper
 */
abstract class TraktTrendMapper<S, D> (
    parentCoroutineJob: Job? = null,
    private val pagingRequestHelper: PagingRequestHelper.Request.Callback? = null
): SupportDataMapper<S, D>(parentCoroutineJob), IMapperHelper<Response<S>> {

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
    override suspend fun handleResponse(deferred: Deferred<Response<S>>): NetworkState {
        val response = deferred.await()
        return if (response.isSuccessful && response.body() != null) {
            val mapped = onResponseMapFrom(response.body()!!)
            onResponseDatabaseInsert(mapped)
            pagingRequestHelper?.recordSuccess()
            NetworkState.LOADED
        } else {
            pagingRequestHelper?.recordFailure(Throwable(response.message()))
            NetworkState(
                status = SupportStateContract.ERROR,
                message = response.message(),
                code = response.code()
            )
        }
    }
}