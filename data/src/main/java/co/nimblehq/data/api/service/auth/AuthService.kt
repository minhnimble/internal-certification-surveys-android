package co.nimblehq.data.api.service.auth

import co.nimblehq.data.api.request.LoginByPasswordWithEmailRequest
import co.nimblehq.data.api.request.RefreshTokenRequest
import co.nimblehq.data.api.response.OAuthResponse
import io.reactivex.Flowable
import retrofit2.http.*

/**
 * Providing APIs to communicate with Nimble Surveys for OAuth services
 */
interface AuthService {

    @POST("/api/v1/oauth/token")
    fun loginByPasswordWithEmail(
        @Body request: LoginByPasswordWithEmailRequest
    ): Flowable<OAuthResponse>

    @POST("/api/v1/oauth/token")
    fun refreshToken(
        @Body request: RefreshTokenRequest
    ): Flowable<OAuthResponse>
}
