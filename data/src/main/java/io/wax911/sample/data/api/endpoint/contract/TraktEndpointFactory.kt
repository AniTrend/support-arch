package io.wax911.sample.data.api.endpoint.contract

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.wax911.sample.data.BuildConfig
import io.wax911.support.data.factory.SupportEndpointFactory
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Retrofit
import kotlin.reflect.KClass

/**
 * Endpoint factory for our application scope
 */
open class TraktEndpointFactory<E: Any>(
    url: String = BuildConfig.apiUrl,
    endpoint: KClass<E>
) : SupportEndpointFactory<E>(url = url, endpoint = endpoint), KoinComponent {

    override val retrofit by inject<Retrofit>()

    companion object {
        val GSON: Gson by lazy {
            GsonBuilder()
                .enableComplexMapKeySerialization()
                .generateNonExecutableJson()
                .serializeNulls()
                .setLenient()
                .create()
        }
    }
}