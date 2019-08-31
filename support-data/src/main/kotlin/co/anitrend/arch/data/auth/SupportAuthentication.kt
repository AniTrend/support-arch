package co.anitrend.arch.data.auth

import co.anitrend.arch.data.auth.contract.ISupportAuthentication

/**
 * An abstract helper class for solving authentication use-cases with tokens which may expire
 * and require refreshing
 *
 * @since v1.1.X
 */
abstract class SupportAuthentication<T, R> : ISupportAuthentication<R> {

    protected val moduleTag: String = javaClass.simpleName

    /**
     * Checks if the data source that contains the token is valid,
     * how to determine the validity of an existing token differs
     * with each token type
     */
    protected abstract fun isTokenValid(): Boolean

    /**
     * Handle invalid token state by either renewing it or un-authenticates
     * the user locally if the token cannot be refreshed
     */
    protected abstract fun onInvalidToken()

    /**
     * Handles complex task or dispatching of token refreshing to the an external work,
     * optionally the implementation can perform these operation internally
     */
    protected abstract fun refreshToken(): Boolean

    /**
     * If using Oauth 2 then once the user has approved your client they will be redirected to your redirect URI,
     * included in the URL fragment will be an access_token parameter that includes the JWT access token
     * used to make requests on their behalf.
     *
     * Otherwise this could just be an authentication result that should be handled and complete the authentication
     * process on the users behalf
     *
     * @param authPayload payload from an authenticating source
     * @return True if the operation was successful, false otherwise
     */
    abstract suspend fun handleCallbackUri(authPayload: T): Boolean
}