package co.nimblehq.data.api.interceptor

import co.nimblehq.data.api.common.event.TokenExpiredEvent
import co.nimblehq.data.api.request.helper.RequestHelper
import co.nimblehq.data.api.response.auth.OAuthResponse
import co.nimblehq.data.api.service.auth.AuthService
import co.nimblehq.data.lib.common.AUTHORIZATION_HEADER
import co.nimblehq.data.lib.rxjava.RxBus
import co.nimblehq.data.lib.schedulers.RxScheduler
import co.nimblehq.data.storage.SecureStorage
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class AppRequestInterceptor @Inject constructor(
    private val securedStorage: SecureStorage,
    private val authService: AuthService
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = executeRequest(chain)
        if (response.code == 401) { // Current token is expired, trigger refresh token API call to get a new token
            val refreshTokenRequest = RequestHelper.refreshToken(
                securedStorage.userRefreshToken ?: ""
            )
            return try {
                val refreshTokenResponse = authService.refreshToken(refreshTokenRequest)
                    .subscribeOn(RxScheduler.IoThread.get())
                    .observeOn(RxScheduler.IoThread.get())
                    .firstOrError()
                    .blockingGet()
                updateToken(refreshTokenResponse)
                executeRequest(chain)
            } catch (e :Throwable) {
                updateToken(null)
                RxBus.publish(TokenExpiredEvent)
                response
            }
        }
        return response
    }

    private fun executeRequest(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        setAuthHeader(builder)
        return chain.proceed(builder.build())
    }

    private fun setAuthHeader(builder: Request.Builder) {
        val tokenType = securedStorage.userTokenType
        val token = securedStorage.userAccessToken
        builder.addHeader(AUTHORIZATION_HEADER, "$tokenType $token")
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
