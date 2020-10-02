package co.nimblehq.usecase.session

import co.nimblehq.data.error.LoginError
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.data.service.repository.auth.AuthRepository
import co.nimblehq.usecase.base.CompletableUseCase
import io.reactivex.Completable
import javax.inject.Inject

class LoginByPasswordCompletableUseCase @Inject constructor(
    rxSchedulerProvider: RxSchedulerProvider,
    private val authRepository: AuthRepository
) : CompletableUseCase<LoginByPasswordCompletableUseCase.Input>(
    rxSchedulerProvider.io(),
    rxSchedulerProvider.main(),
    ::LoginError
) {

    data class Input(
        val email: String,
        val password: String
    )

    override fun create(input: Input): Completable {
        return with(input) {
            authRepository.loginByPasswordWithEmail(
                email,
                password
            )
        }
    }
}
