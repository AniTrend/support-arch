package io.wax911.sample.core.auth.contract

import android.net.Uri
import io.wax911.sample.core.auth.model.JsonWebToken
import io.wax911.support.core.util.SupportCoroutineHelper

interface IAuthenticationHelper: SupportCoroutineHelper {

    val jsonWebToken: JsonWebToken?

    /**
     * Once the user has approved your client they will be redirected to your redirect URI,
     * included in the URL fragment will be an access_token parameter that includes the JWT access token
     * used to make requests on their behalf.
     *
     * @param callbackUri callback uri from authentication process
     * @return True if the operation was successful, false otherwise
     */
    suspend fun handleCallbackUri(callbackUri: Uri): Boolean
}