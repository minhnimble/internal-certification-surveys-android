package co.nimblehq.data.provider

import co.nimblehq.data.api.service.auth.AuthService
import co.nimblehq.data.api.service.survey.SurveyService
import co.nimblehq.data.repository.AuthRepository
import co.nimblehq.data.repository.AuthRepositoryImpl
import co.nimblehq.data.repository.SurveyRepository
import co.nimblehq.data.repository.SurveyRepositoryImpl

class ApiRepositoryProvider {
    companion object {
        fun getAuthRepository(
            authService: AuthService
        ): AuthRepository {
            return AuthRepositoryImpl(authService)
        }

        fun getSurveyRepository(
            surveyService: SurveyService
        ): SurveyRepository {
            return SurveyRepositoryImpl(surveyService)
        }
    }
}
