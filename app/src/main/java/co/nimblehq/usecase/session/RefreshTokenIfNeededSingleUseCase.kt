package co.nimblehq.usecase.session

import co.nimblehq.data.authenticator.TokenRefresher
import co.nimblehq.data.error.RefreshTokenError
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.data.model.AuthData
import co.nimblehq.data.repository.AuthRepository
import co.nimblehq.usecase.base.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class RefreshTokenIfNeededSingleUseCase @Inject constructor(
    rxSchedulerProvider: RxSchedulerProvider,
    private val tokenRefresher: TokenRefresher
) : SingleUseCase<AuthData, AuthData>(
    rxSchedulerProvider.io(),
    rxSchedulerProvider.io(),
    ::RefreshTokenError
) {

    override fun create(input: AuthData): Single<AuthData> {
        return if (input.isExpired) {
            tokenRefresher.refreshToken(
                input.refreshToken
            )
        } else {
            Single.just(input)
        }
    }
}
