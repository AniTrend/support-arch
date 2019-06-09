package io.wax911.support.data.source.mapper

import androidx.lifecycle.MutableLiveData
import io.wax911.support.data.model.NetworkState
import io.wax911.support.data.model.contract.SupportStateType
import io.wax911.support.data.source.mapper.contract.ISupportDataMapper
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

/**
 * Provides functionality for mapping objects from one type to another
 *
 * @param parentCoroutineJob parent coroutine from something that is lifecycle aware,
 * this enables us to cancels jobs automatically when the parent is also canceled
 */
abstract class SupportDataMapper<S, D>(
    parentCoroutineJob: Job? = null,
    private val networkState: MutableLiveData<NetworkState>
) : ISupportDataMapper<S, D> {

    protected val moduleTag: String = javaClass.simpleName

    /**
     * Requires an instance of [kotlinx.coroutines.Job] or [kotlinx.coroutines.SupervisorJob]
     */
    override val supervisorJob = SupervisorJob(parentCoroutineJob)

    override val responseCallback = object : Callback<S> {

        /**
         * Invoked when a network exception occurred talking to the server or when an unexpected
         * exception occurred creating the request or processing the response.
         */
        override fun onFailure(call: Call<S>, throwable: Throwable) {
            networkState.postValue(
                NetworkState.error(
                    msg = throwable.localizedMessage
                )
            )
            Timber.tag(moduleTag).e(throwable)
        }

        /**
         * Invoked for a received HTTP response.
         *
         * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
         * Call [Response.isSuccessful] to determine if the response indicates success.
         */
        override fun onResponse(call: Call<S>, response: Response<S?>) {
            when (response.isSuccessful) {
                true -> {
                    launch {
                        val mapped = onResponseMapFrom(response)
                        onResponseDatabaseInsert(mapped)
                        withContext(Dispatchers.Main) {
                            networkState.postValue(
                                NetworkState.LOADED
                            )
                        }
                    }
                }
                false -> {
                    val message = response.message()
                    Timber.tag(moduleTag).e(message)
                    networkState.postValue(
                        NetworkState(
                            status = SupportStateType.ERROR,
                            message = message,
                            code = response.code()
                        )
                    )
                }
            }
        }
    }
}