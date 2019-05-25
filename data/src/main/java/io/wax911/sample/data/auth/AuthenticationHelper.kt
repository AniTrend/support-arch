package io.wax911.sample.data.auth

import android.net.Uri
import io.wax911.sample.data.BuildConfig
import io.wax911.sample.data.auth.contract.IAuthenticationHelper
import io.wax911.sample.data.auth.model.JsonWebToken
import io.wax911.sample.data.dao.DatabaseHelper

class AuthenticationHelper(private val databaseHelper: DatabaseHelper): IAuthenticationHelper {

    override val jsonWebToken: JsonWebToken?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    /**
     * Once the user has approved your client they will be redirected to your redirect URI,
     * included in the URL fragment will be an access_token parameter that includes the JWT access token
     * used to make requests on their behalf.
     *
     * @param callbackUri callback uri from authentication process
     * @return True if the operation was successful, false otherwise
     */
    override suspend fun handleCallbackUri(callbackUri: Uri): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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