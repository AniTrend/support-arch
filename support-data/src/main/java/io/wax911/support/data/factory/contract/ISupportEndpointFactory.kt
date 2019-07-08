package io.wax911.support.data.factory.contract

/**
 * Contract for constructing end point factories
 */
interface ISupportEndpointFactory<S> {

    /**
     * Creates a retrofit service interface using the retrofit instance
     */
    fun create(): S
}