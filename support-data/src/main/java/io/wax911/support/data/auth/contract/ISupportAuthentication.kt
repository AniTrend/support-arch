package io.wax911.support.data.auth.contract

import okhttp3.Request

interface ISupportAuthentication {

    /**
     * Facade to provide information on authentication status of the application,
     * on demand
     */
    val isAuthenticated: Boolean

    /**
     * Injects authentication headers if the application was authenticated,
     * otherwise non
     *
     * @param requestBuilder
     */
    fun injectHeaders(requestBuilder: Request.Builder)
}