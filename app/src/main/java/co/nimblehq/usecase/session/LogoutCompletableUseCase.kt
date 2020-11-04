package co.nimblehq.usecase.session

import co.nimblehq.data.error.SessionError.LogoutError
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.data.repository.AuthRepository
import co.nimblehq.usecase.base.CompletableUseCase
import io.reactivex.Completable
import javax.inject.Inject

class LogoutCompletableUseCase @Inject constructor(
    rxSchedulerProvider: RxSchedulerProvider,
    private val authRepository: AuthRepository
) : CompletableUseCase<String>(
    rxSchedulerProvider.io(),
    rxSchedulerProvider.main(),
    ::LogoutError
) {

    override fun create(input: String): Completable {
        return authRepository.logout(input)
    }
}
