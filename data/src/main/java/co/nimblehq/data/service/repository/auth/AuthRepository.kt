package co.nimblehq.data.service.repository.auth

import co.nimblehq.data.service.response.OAuthResponse
import io.reactivex.Completable
import co.nimblehq.data.service.request.helper.RequestHelper
import co.nimblehq.data.storage.SecureStorage
import javax.inject.Inject

interface AuthRepository {

    fun loginByPasswordWithEmail(
        email: String,
        password: String
    ): Completable


    fun clearAccessToken(): Completable
}

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val secureStorage: SecureStorage
) : AuthRepository {

    override fun clearAccessToken(): Completable {
        return Completable.fromAction {
            secureStorage.userAccessToken = null
            secureStorage.userAccessTokenCreatedAt = null
            secureStorage.userAccessTokenExpiresIn = null
            secureStorage.userTokenType = null
        }
    }

    override fun loginByPasswordWithEmail(
        email: String,
        password: String
    ): Completable {
        return authService
            .loginByPasswordWithEmail(RequestHelper.loginWithEmailRequest(email, password))
            .firstOrError()
            .doOnSuccess { updateAccessToken(it) }
            .ignoreElement()
    }

    private fun updateAccessToken(response: OAuthResponse?) {
        secureStorage.apply {
            userAccessToken = response?.data?.attributes?.accessToken
            userAccessTokenCreatedAt = response?.data?.attributes?.createdAt
            userAccessTokenExpiresIn = response?.data?.attributes?.expiresIn
            userRefreshToken = response?.data?.attributes?.refreshToken
            userTokenType = response?.data?.attributes?.tokenType
        }
    }
}
