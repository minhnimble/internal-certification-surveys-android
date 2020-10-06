package co.nimblehq.data.service.repository.auth

import io.reactivex.Completable
import co.nimblehq.data.service.request.helper.RequestHelper
import co.nimblehq.data.service.response.OAuthResponse
import io.reactivex.Single
import javax.inject.Inject

interface AuthRepository {

    fun loginByPasswordWithEmail(
        email: String,
        password: String
    ): Single<OAuthResponse>
}

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService
) : AuthRepository {

    override fun loginByPasswordWithEmail(
        email: String,
        password: String
    ): Single<OAuthResponse> {
        return authService
            .loginByPasswordWithEmail(RequestHelper.loginWithEmailRequest(email, password))
            .firstOrError()
    }
}
