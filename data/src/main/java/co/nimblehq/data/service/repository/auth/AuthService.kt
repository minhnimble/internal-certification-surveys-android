package co.nimblehq.data.service.repository.auth

import co.nimblehq.data.service.request.LoginByPasswordWithEmailRequest
import co.nimblehq.data.service.response.OAuthResponse
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
}
