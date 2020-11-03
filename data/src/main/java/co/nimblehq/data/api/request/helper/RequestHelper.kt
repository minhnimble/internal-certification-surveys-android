package co.nimblehq.data.api.request.helper

import co.nimblehq.data.lib.common.OAUTH_GRANT_TYPE_PASSWORD
import co.nimblehq.data.api.common.secrets.Secrets
import co.nimblehq.data.api.request.AnswerRequest
import co.nimblehq.data.api.request.OAuthRequest.*
import co.nimblehq.data.api.request.QuestionRequest
import co.nimblehq.data.api.request.SubmitSurveyRequest
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

    fun refreshToken(refreshToken: String): RefreshToken {
        return RefreshToken(
            OAUTH_GRANT_TYPE_REFRESH_TOKEN,
            refreshToken,
            Secrets.clientId,
            Secrets.clientSecret
        )
    }

    fun submitSurveyAnswers(
        surveyId: String,
        questions: List<QuestionRequest>
    ): SubmitSurveyRequest {
        return SubmitSurveyRequest(
            surveyId,
            questions
        )
    }

    fun surveyQuestion(
        questionId: String,
        answers: List<AnswerRequest>
    ): QuestionRequest {
        return QuestionRequest(
            questionId,
            answers
        )
    }

    fun surveyAnswer(
        answerId: String,
        answer: String?
    ): AnswerRequest {
        return AnswerRequest(
            answerId,
            answer
        )
    }
}
