package co.nimblehq.data.api.request.helper

import co.nimblehq.data.api.common.secrets.Secrets
import co.nimblehq.data.api.request.OAuthRequest.*
import co.nimblehq.data.api.request.QuestionResponsesRequest
import co.nimblehq.data.api.request.SubmitSurveyResponsesRequest
import co.nimblehq.data.lib.common.OAUTH_GRANT_TYPE_PASSWORD
import co.nimblehq.data.lib.common.OAUTH_GRANT_TYPE_REFRESH_TOKEN

object RequestHelper {

    fun loginWithEmail(
        email: String,
        password: String
    ): LoginByPasswordWithEmail {
        return LoginByPasswordWithEmail(
            OAUTH_GRANT_TYPE_PASSWORD,
            email,
            password,
            Secrets.clientId,
            Secrets.clientSecret
        )
    }

    fun logout(token: String): Logout {
        return Logout(
            token,
            Secrets.clientId,
            Secrets.clientSecret
        )
    }

    fun refreshToken(refreshToken: String): RefreshToken {
        return RefreshToken(
            OAUTH_GRANT_TYPE_REFRESH_TOKEN,
            refreshToken,
            Secrets.clientId,
            Secrets.clientSecret
        )
    }

    fun submitSurveyResponses(
        surveyId: String,
        responses: List<QuestionResponsesRequest>
    ): SubmitSurveyResponsesRequest {
        return SubmitSurveyResponsesRequest(
            surveyId,
            responses
        )
    }
}
