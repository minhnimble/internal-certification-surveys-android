package co.nimblehq.data.service.repository.auth

import co.nimblehq.data.lib.common.CONTENT_TYPE_HEADER
import co.nimblehq.data.lib.common.HTTP_HEADER_CONTENT_TYPE_APPLICATION_JSON
import co.nimblehq.data.service.request.AuthRequest
import co.nimblehq.data.service.response.OAuthResponse
import io.reactivex.Flowable
import retrofit2.http.*

/**
 * Providing APIs to communicate with Nimble Surveys for OAuth services
 */
interface AuthService {

    @POST("/api/v1/oauth/token")
    fun loginByPasswordWithEmail(
        @Body request: AuthRequest.LoginByPasswordWithEmail,
        @Header(CONTENT_TYPE_HEADER) contentType: String = HTTP_HEADER_CONTENT_TYPE_APPLICATION_JSON
    ): Flowable<OAuthResponse>
}
