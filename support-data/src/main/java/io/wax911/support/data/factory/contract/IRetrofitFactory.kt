package io.wax911.support.data.factory.contract

@Deprecated(
    message = "Will be removed in release v1.2.X",
    replaceWith = ReplaceWith(
        expression = "[io.wax911.support.data.factory.SupportEndpointFactory]"
    ),
    level = DeprecationLevel.WARNING
)
/**
 * Retrofit service locator contract
 *
 * @since v1.0.X
 */
interface IRetrofitFactory {

    /**
     * Generates retrofit service classes
     *
     * @param serviceClass The interface class method representing your request to use
     */
    fun <S> createService(serviceClass: Class<S>): S
}

/**
 * Creates a retrofit endpoint of the given class of type <T>
 */
inline fun <reified T> IRetrofitFactory.getEndPointOf() : T =
    createService(T::class.java)