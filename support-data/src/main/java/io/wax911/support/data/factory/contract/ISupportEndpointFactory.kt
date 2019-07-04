package io.wax911.support.data.factory.contract

interface ISupportEndpointFactory<S> {
    fun create(): S
}