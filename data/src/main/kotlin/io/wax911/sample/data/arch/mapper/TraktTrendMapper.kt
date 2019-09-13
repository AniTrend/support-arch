package io.wax911.sample.data.arch.mapper

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingRequestHelper
import co.anitrend.arch.data.mapper.SupportResponseMapper
import co.anitrend.arch.data.mapper.contract.ISupportResponseHelper
import co.anitrend.arch.domain.entities.NetworkState
import retrofit2.Call
import timber.log.Timber

/**
 * TraktTrendMapper specific mapper, extends and overrides the [invoke] callback
 * this makes it easier for us to implement error logging and provide better error messages
 *
 * @see SupportResponseMapper
 */
abstract class TraktTrendMapper<S, D>: SupportResponseMapper<S, D>(), ISupportResponseHelper<Call<S>> {

    /**
     * Response handler for coroutine contexts, mainly for paging
     *
     * @param resource awaiting execution
     * @param pagingRequestHelper optional paging request callback
     */
    override suspend fun invoke(
        resource: Call<S>,
        pagingRequestHelper: PagingRequestHelper.Request.Callback
    ) {
        val result = runCatching {
            val response = resource.execute()
            if (response.isSuccessful && response.body() != null) {
                val mapped = onResponseMapFrom(response.body()!!)
                onResponseDatabaseInsert(mapped)
                pagingRequestHelper.recordSuccess()
            } else {
                pagingRequestHelper.recordFailure(
                    Throwable(response.message())
                )
            }
        }

        result.getOrElse {
            it.printStackTrace()
            Timber.tag(moduleTag).e(it)
        }
    }

    /**
     * Response handler for coroutine contexts which need to observe [NetworkState]
     *
     * @param resource awaiting execution
     * @return [NetworkState] for the deferred result
     */
    override suspend fun invoke(resource: Call<S>): NetworkState {
        val result = runCatching {
            val response = resource.execute()
            if (response.isSuccessful && response.body() != null) {
                val mapped = onResponseMapFrom(response.body()!!)
                onResponseDatabaseInsert(mapped)
                NetworkState.Success
            } else {
                NetworkState.Error(
                    heading = response.message(),
                    message = response.errorBody()?.string(),
                    code = response.code()
                )
            }
        }

        return result.getOrElse {
            it.printStackTrace()
            Timber.tag(moduleTag).e(it)
            NetworkState.Error(
                heading = "Internal Application Error",
                message = it.message
            )
        }
    }

    /**
     * Response handler for coroutine contexts which need to observe [NetworkState]
     *
     * @param resource awaiting execution
     * @param networkState for the deferred result
     */
    override suspend fun invoke(resource: Call<S>, networkState: MutableLiveData<NetworkState>) {
        val state = invoke(resource)
        networkState.postValue(state)
    }
}