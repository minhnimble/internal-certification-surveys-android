package co.nimblehq.usecase.session

import co.nimblehq.data.error.LoginError
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.data.service.repository.auth.AuthRepository
import co.nimblehq.usecase.base.CompletableUseCase
import io.reactivex.Completable
import javax.inject.Inject

class LoginByPasswordUseCase @Inject constructor(
    rxSchedulerProvider: RxSchedulerProvider,
    private val authRepository: AuthRepository
) : CompletableUseCase<Pair<String, String>>(
    rxSchedulerProvider.io(),
    rxSchedulerProvider.main(),
    ::LoginError
) {

    override fun create(input: Pair<String, String>): Completable {
        val (email, password) = input

        return authRepository.loginByPasswordWithEmail(
                email,
                password
        )
    }
}
