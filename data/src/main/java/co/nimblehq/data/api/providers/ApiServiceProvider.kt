package co.nimblehq.data.api.providers

import co.nimblehq.data.api.service.auth.AuthService
import co.nimblehq.data.api.service.survey.SurveyService
import co.nimblehq.data.api.service.user.UserService
import retrofit2.Retrofit

class ApiServiceProvider {
    companion object {
        fun getAuthService(retrofit: Retrofit): AuthService {
            return retrofit.create(AuthService::class.java)
        }

        fun getSurveyService(retrofit: Retrofit): SurveyService {
            return retrofit.create(SurveyService::class.java)
        }

        fun getUserService(retrofit: Retrofit): UserService {
            return retrofit.create(UserService::class.java)
        }
    }
}
