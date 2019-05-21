package io.wax911.sample.core.api.interceptor

import android.content.Context
import io.wax911.sample.core.auth.AuthenticationHelper
import io.wax911.sample.core.auth.contract.IAuthenticationHelper
import io.wax911.sample.core.dao.DatabaseHelper
import io.wax911.sample.core.util.Settings
import io.wax911.support.extension.isConnectedToNetwork
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

/**
 * Authentication interceptor add headers dynamically when the application is authenticated.
 * The context in which an [Interceptor] may be  parallel or asynchronous depending
 * on the dispatching caller, as such take care to assure thread safety
 */
class AuthInterceptor(private val context: Context?) : Interceptor, KoinComponent {

    private val authenticationHelper  by inject<IAuthenticationHelper>()
    private val databaseHelper by inject<DatabaseHelper>()
    private val settings by inject<Settings>()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()

        if (settings.isAuthenticated) {
            val jsonWebToken = authenticationHelper.jsonWebToken
            when {
                jsonWebToken != null -> builder.addHeader(
                    AuthenticationHelper.AUTHORIZATION,
                    jsonWebToken.getTokenKey()
                )
                context.isConnectedToNetwork() -> {
                    settings.authenticatedUserId = Settings.INVALID_USER_ID
                    settings.isAuthenticated = false
                    databaseHelper.clearAllTables()
                    Timber.e("${toString()} -> authentication token is null, application is logging user out!")
                }
                else ->
                    Timber.i("${toString()} -> Cannot refresh authentication token, application currently offline.")
            }
        }

        return chain.proceed(builder.build())
    }
}
