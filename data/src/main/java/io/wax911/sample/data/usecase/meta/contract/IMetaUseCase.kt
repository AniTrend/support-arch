package io.wax911.sample.data.usecase.meta.contract

import io.wax911.support.data.model.NetworkState
import io.wax911.sample.data.model.meta.MediaCategory
import io.wax911.support.data.usecase.coroutine.ISupportCoroutineUseCase

interface IMetaUseCase :
    ISupportCoroutineUseCase<IMetaUseCase.Payload, NetworkState> {

    /**
     * Reusable payload for meta use cases
     */
    data class Payload(
        val mediaCategory: MediaCategory
    )
}