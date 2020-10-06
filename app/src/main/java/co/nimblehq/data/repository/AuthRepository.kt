package co.nimblehq.data.repository

import co.nimblehq.data.model.AuthData
import co.nimblehq.data.model.toAuthData
import co.nimblehq.data.api.service.auth.AuthService
import co.nimblehq.data.api.request.helper.RequestHelper
import io.reactivex.Single
import javax.inject.Inject

interface AuthRepository {

    fun loginByPasswordWithEmail(
        email: String,
        password: String
    ): Single<AuthData>
}

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService
) : AuthRepository {

    override fun loginByPasswordWithEmail(
        email: String,
        password: String
    ): Single<AuthData> {
        return authService
            .loginByPasswordWithEmail(RequestHelper.loginWithEmailRequest(email, password))
            .firstOrError()
            .map { it.toAuthData() }
    }
}