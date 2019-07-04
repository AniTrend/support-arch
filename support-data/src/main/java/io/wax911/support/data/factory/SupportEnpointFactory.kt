package io.wax911.support.data.factory

import io.wax911.support.data.factory.contract.ISupportEndpointFactory
import okhttp3.Interceptor
import org.koin.core.KoinComponent
import retrofit2.Retrofit
import kotlin.reflect.KClass

/**
 * Generates retrofit service classes
 *
 * @param url The url to use when create a service endpoint
 * @param endpoint The interface class method representing your request to use
 */
abstract class EndpointFactory<S: Any>(
    private val url: String,
    private val endpoint: KClass<S>
) : ISupportEndpointFactory<S>, KoinComponent {

    protected abstract val clientInterceptor: Interceptor

    protected abstract val retrofit: Retrofit

    override fun create(): S = retrofit.create(endpoint.java)
}