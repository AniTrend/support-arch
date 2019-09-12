package co.anitrend.arch.data.auth.contract

/**
 * Contract for token authentication use-cases
 *
 * @since v1.1.X
 */
interface ISupportAuthentication {

    val moduleTag: String

    /**
     * Facade to provide information on authentication status of the application,
     * on demand
     */
    val isAuthenticated: Boolean
}