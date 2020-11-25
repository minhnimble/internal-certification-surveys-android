package co.nimblehq.data.api.authenticator

import co.nimblehq.data.api.common.listener.AppTokenExpiredNotifier
import co.nimblehq.data.api.request.helper.RequestHelper
import co.nimblehq.data.api.response.auth.OAuthResponse
import co.nimblehq.data.api.service.auth.AuthService
import co.nimblehq.data.lib.common.AUTHORIZATION_HEADER
import co.nimblehq.data.lib.schedulers.RxScheduler
import co.nimblehq.data.storage.SecureStorage
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException
import javax.inject.Inject

class AppRequestAuthenticator @Inject constructor(
    private val securedStorage: SecureStorage,
    private val authService: AuthService
) : Authenticator {

    /**
     * Authenticator for when the authToken need to be refresh and updated
     * every time we get a 401 error code
     */
    @Throws(IOException::class)
    override fun authenticate(route: Route?, response: Response): Request? {
        return try {
            synchronized(this) {
                if (isValidToken() && !isExpiredToken()) {
                    // Have valid token and not expired token, just trigger the unauthorized request again
                    return handleRefreshTokenSuccess(response = response)
                }

                // Get new token from backend
                val refreshTokenRequest = RequestHelper.refreshToken(securedStorage.userRefreshToken ?: "")
                val refreshTokenResponse = authService.refreshToken(refreshTokenRequest)
                    .subscribeOn(RxScheduler.IoThread.get())
                    .observeOn(RxScheduler.IoThread.get())
                    .blockingGet()
                updateToken(refreshTokenResponse)

                // Avoid infinite loop case if the new token is not valid or is expired that caused 401 error
                if (!isValidToken() || isExpiredToken()) {
                    return handleRefreshTokenFailed()
                }

                // Apply the new token and trigger the unauthorized request again
                handleRefreshTokenSuccess(response)
            }
        } catch (e: Throwable) {
            handleRefreshTokenFailed(e)
        }
    }

    private fun handleRefreshTokenFailed(error: Throwable? = null): Request? {
        updateToken(null)
        AppTokenExpiredNotifier.requestTokenFailed(error)
        return null
    }

    private fun handleRefreshTokenSuccess(response: Response): Request {
        val builder = response.request.newBuilder()
        val newTokenHeader = generateTokenHeader()
        builder.header(AUTHORIZATION_HEADER, newTokenHeader)
        return builder.build()
    }

    private fun generateTokenHeader(): String {
        val tokenType = securedStorage.userTokenType
        val token = securedStorage.userAccessToken
        return "$tokenType $token"
    }

    private fun isValidToken(): Boolean {
        return with(securedStorage) {
            !userAccessToken.isNullOrEmpty() && !userTokenType.isNullOrEmpty() && !userRefreshToken.isNullOrEmpty()
        }
    }

    private fun isExpiredToken(): Boolean {
        return with(securedStorage) {
            System.currentTimeMillis() / 1000 >= (userAccessTokenCreatedAt ?: 0) + (userAccessTokenExpiresIn ?: 0)
        }
    }

    private fun updateToken(response: OAuthResponse?) {
        securedStorage.apply {
            with(response?.data?.attributes) {
                userAccessToken = this?.accessToken
                userAccessTokenCreatedAt = this?.createdAt
                userAccessTokenExpiresIn = this?.expiresIn
                userRefreshToken = this?.refreshToken
                userTokenType = this?.tokenType
            }
        }
    }
}
