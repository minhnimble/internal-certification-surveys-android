package co.nimblehq.usecase.session

import co.nimblehq.data.error.Ignored
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.data.model.AuthData
import co.nimblehq.data.storage.SecureStorage
import co.nimblehq.usecase.base.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class GetUserTokenSingleUseCase @Inject constructor(
    rxSchedulerProvider: RxSchedulerProvider,
    private val secureStorage: SecureStorage
) : SingleUseCase<Unit, AuthData>(rxSchedulerProvider.io(), rxSchedulerProvider.io(), ::Ignored) {

    override fun create(input: Unit): Single<AuthData> {
        return Single.fromCallable {
            secureStorage.userAccessToken
            AuthData(
                secureStorage.userAccessToken ?: "",
                secureStorage.userAccessTokenCreatedAt ?: 0,
                secureStorage.userAccessTokenExpiresIn ?: 0,
                secureStorage.userRefreshToken ?: "",
                secureStorage.userTokenType ?: ""
            )
        }
            .filter { it.isValid }
            .doOnError { throw Throwable("Refresh token not found") }
            .toSingle()
    }
}
