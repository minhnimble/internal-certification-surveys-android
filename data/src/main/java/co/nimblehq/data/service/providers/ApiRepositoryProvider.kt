package co.nimblehq.data.service.providers

import co.nimblehq.data.service.repository.auth.AuthRepository
import co.nimblehq.data.service.repository.auth.AuthRepositoryImpl
import co.nimblehq.data.service.repository.auth.AuthService
import co.nimblehq.data.storage.SecureStorage

class ApiRepositoryProvider {
    companion object {
        fun getAuthRepository(
            authService: AuthService
        ): AuthRepository {
            return AuthRepositoryImpl(authService)
        }
    }
}
