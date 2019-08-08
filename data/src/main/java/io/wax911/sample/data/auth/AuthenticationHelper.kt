package io.wax911.sample.data.auth

import android.net.Uri
import io.wax911.sample.data.BuildConfig
import io.wax911.sample.data.dao.query.JsonWebTokenDao
import io.wax911.sample.data.util.Settings
import io.wax911.support.data.auth.SupportAuthentication
import io.wax911.support.extension.empty
import io.wax911.support.extension.util.SupportConnectivityHelper
import okhttp3.Request
import timber.log.Timber

class AuthenticationHelper(
    private val connectivityHelper: SupportConnectivityHelper,
    private val jsonWebTokenDao: JsonWebTokenDao,
    private val settings: Settings
): SupportAuthentication<Uri, Request.Builder>() {

    /**
     * Facade to provide information on authentication status of the application,
     * on demand
     */
    override val isAuthenticated: Boolean
        get() = settings.isAuthenticated

    /**
     * Checks if the data source that contains the token is valid
     */
    override fun isTokenValid(): Boolean {
        val token = jsonWebTokenDao.findLatest()
        return token == null
    }

    /**
     * Handles complex task or dispatching of token refreshing to the an external work,
     * optionally the implementation can perform these operation internally
     */
    override fun refreshToken(): Boolean {
        // handle logic to refresh the token on the users behalf
        if (!isTokenValid())
            return false
        return true
    }

    /**
     * Performs core operation of applying authentication credentials
     * at runtime
     *
     * @param resource object that need to be manipulated
     */
    override fun invoke(resource: Request.Builder) {
        assert(isAuthenticated)
        when (refreshToken()) {
            true -> {
                val latestToken = jsonWebTokenDao.findLatest()
                val tokenHeader = latestToken?.getTokenKey() ?: String.empty()
                resource.addHeader(
                    AUTHORIZATION,
                    tokenHeader
                )
            }
            false -> onInvalidToken()
        }
    }

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
    override suspend fun handleCallbackUri(authPayload: Uri): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Handle invalid token state by either renewing it or un-authenticates
     * the user locally if the token cannot be refreshed
     */
    override fun onInvalidToken() {
        // TODO("Implement token retry count threshold for each authentication attempt")
        if (connectivityHelper.isConnected) {
            settings.authenticatedUserId = Settings.INVALID_USER_ID
            settings.isAuthenticated = false
            jsonWebTokenDao.clearTable()
            Timber.tag(moduleTag).e("Authentication token is null, application is logging user out!")
        }
    }

    companion object {

        const val CALLBACK_QUERY_KEY = "access_token"

        const val AUTHORIZATION = "Authorization"

        val authenticationUri: Uri by lazy {
            Uri.Builder()
                .scheme("https")
                .authority(BuildConfig.apiAuthUrl)
                .appendPath("authorize")
                .appendQueryParameter("client_id", BuildConfig.clientId)
                .appendQueryParameter("response_type", "token")
                .build()
        }
    }
}