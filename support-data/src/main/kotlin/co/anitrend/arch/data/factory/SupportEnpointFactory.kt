package co.anitrend.arch.data.factory

import co.anitrend.arch.data.factory.contract.ISupportEndpointFactory
import retrofit2.Retrofit
import kotlin.reflect.KClass

/**
 * An abstract helper to generate retrofit service classes through companion objects
 *
 * @param url The url to use when create a service endpoint
 * @param endpoint The interface class method representing your request to use
 *
 * @since v1.1.0
 */
abstract class SupportEndpointFactory<S: Any>(
    protected val url: String,
    protected val endpoint: KClass<S>
) : ISupportEndpointFactory<S> {

    protected abstract val retrofit: Retrofit

    /**
     * Creates a retrofit service interface using the retrofit instance
     */
    override fun create(): S = retrofit.create(endpoint.java)
}