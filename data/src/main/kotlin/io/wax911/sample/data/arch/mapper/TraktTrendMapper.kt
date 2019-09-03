package io.wax911.sample.data.arch.mapper

import androidx.paging.PagingRequestHelper
import co.anitrend.arch.data.mapper.SupportDataMapper
import co.anitrend.arch.data.mapper.contract.IMapperHelper
import co.anitrend.arch.domain.entities.NetworkState
import retrofit2.Call
import timber.log.Timber

/**
 * TraktTrendMapper specific mapper, extends and overrides the [invoke] callback
 * this makes it easier for us to implement error logging and provide better error messages
 *
 * @see SupportDataMapper
 */
abstract class TraktTrendMapper<S, D> (
    private val pagingRequestHelper: PagingRequestHelper.Request.Callback? = null
): SupportDataMapper<S, D>(), IMapperHelper<Call<S>> {

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
                pagingRequestHelper?.recordSuccess()
                NetworkState.Success
            } else {
                pagingRequestHelper?.recordFailure(
                    Throwable(response.message())
                )
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
}