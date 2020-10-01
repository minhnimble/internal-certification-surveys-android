package co.nimblehq.data.service.providers

import co.nimblehq.data.service.repository.auth.AuthService
import retrofit2.Retrofit

class ApiServiceProvider {
    companion object {
        fun getAuthService(retrofit: Retrofit): AuthService {
            return retrofit.create(AuthService::class.java)
        }
    }
}
