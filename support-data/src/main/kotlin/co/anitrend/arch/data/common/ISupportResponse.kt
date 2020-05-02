package co.anitrend.arch.data.common

import androidx.lifecycle.MutableLiveData
import co.anitrend.arch.domain.entities.NetworkState

/**
 * Contract mapper for wrapping future requests
 *
 * @since v1.1.0
 */
interface ISupportResponse<in RESOURCE, out RESPONSE> {

    /**
     * Response handler for coroutine contexts which need to observe [NetworkState]
     *
     * @param resource awaiting execution
     * @param networkState for the deferred result
     *
     * @return resource fetched if present
     */
    suspend operator fun invoke(
        resource: RESOURCE,
        networkState: MutableLiveData<NetworkState>
    ) : RESPONSE?
}