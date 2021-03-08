package co.anitrend.arch.data.common

import co.anitrend.arch.data.request.callback.RequestCallback
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
     * @param requestCallback for the deferred result
     *
     * @return resource fetched if present
     */
    suspend operator fun invoke(
        resource: RESOURCE,
        requestCallback: RequestCallback
    ) : RESPONSE?
}