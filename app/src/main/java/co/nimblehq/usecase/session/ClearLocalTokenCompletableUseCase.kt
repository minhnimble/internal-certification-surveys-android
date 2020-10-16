package co.nimblehq.usecase.session

import co.nimblehq.data.error.Ignored
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.data.storage.SecureStorage
import co.nimblehq.usecase.base.CompletableUseCase
import io.reactivex.Completable
import javax.inject.Inject

class ClearLocalTokenCompletableUseCase @Inject constructor(
    rxSchedulerProvider: RxSchedulerProvider,
    private val secureStorage: SecureStorage
) : CompletableUseCase<Unit>(
    rxSchedulerProvider.io(),
    rxSchedulerProvider.io(),
    ::Ignored
) {

    override fun create(input: Unit): Completable {
        return Completable.fromAction {
            secureStorage.userAccessToken = null
            secureStorage.userAccessTokenCreatedAt = null
            secureStorage.userAccessTokenExpiresIn = null
            secureStorage.userTokenType = null
        }
    }
}
