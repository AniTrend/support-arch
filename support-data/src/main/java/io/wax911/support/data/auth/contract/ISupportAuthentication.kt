package io.wax911.support.data.auth.contract

/**
 * Contract for token authentication use-cases
 *
 * @since v1.1.X
 */
interface ISupportAuthentication<R> {

    /**
     * Facade to provide information on authentication status of the application,
     * on demand
     */
    val isAuthenticated: Boolean

    /**
     * Performs core operation of applying authentication credentials
     * at runtime
     *
     * @param resource object that need to be manipulated
     */
    operator fun invoke(resource: R)
}