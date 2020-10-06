package co.nimblehq.data.api.providers

import co.nimblehq.data.api.service.auth.AuthService
import retrofit2.Retrofit

class ApiServiceProvider {
    companion object {
        fun getAuthService(retrofit: Retrofit): AuthService {
            return retrofit.create(AuthService::class.java)
        }
    }
}
