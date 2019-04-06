package io.wax911.sample.api.interceptor

import android.content.Context
import io.wax911.sample.extension.getDatabase
import io.wax911.sample.util.AuthenticationUtil
import io.wax911.sample.util.Settings
import io.wax911.sample.util.StateUtil
import io.wax911.support.extension.isConnectedToNetwork
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class AuthInterceptor(private val settings: Settings, private val context: Context?) : Interceptor {

    private val authenticationUtil by lazy {
        AuthenticationUtil(databaseHelper?.webTokenDao())
    }

    private val databaseHelper by lazy {
        context?.getDatabase()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()

        if (settings.isAuthenticated) {
            val webToken = authenticationUtil.getWebToken()
            when {
                webToken != null -> builder.addHeader(StateUtil.authorization, webToken.getHeader())
                context.isConnectedToNetwork() -> {
                    settings.isAuthenticated = false
                    settings.setAuthenticatedUser()
                    databaseHelper?.clearAllTables()
                    Timber.e(toString(), "Looks like the authentication token is null, application is logging user out!")
                }
                else ->
                    Timber.i(toString(), "Cannot refresh authentication token, application currently offline.")
            }
        }

        return chain.proceed(builder.build())
    }
}
