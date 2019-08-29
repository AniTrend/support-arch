package io.wax911.support.data.factory.contract

/**
 * Contract for constructing end point factories
 *
 * @since v1.1.0
 */
interface ISupportEndpointFactory<S> {

    /**
     * Creates a retrofit service interface using the retrofit instance
     */
    fun create(): S
}