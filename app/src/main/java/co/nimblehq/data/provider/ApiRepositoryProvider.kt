package co.nimblehq.data.provider

import co.nimblehq.data.api.service.survey.AuthService
import co.nimblehq.data.repository.AuthRepository
import co.nimblehq.data.repository.AuthRepositoryImpl

class ApiRepositoryProvider {
    companion object {
        fun getAuthRepository(
            authService: AuthService
        ): AuthRepository {
            return AuthRepositoryImpl(authService)
        }
    }
}
