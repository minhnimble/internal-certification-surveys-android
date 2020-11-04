package co.nimblehq.usecase.session

import co.nimblehq.data.error.SessionError.LoginError
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.data.model.AuthData
import co.nimblehq.data.repository.AuthRepository
import co.nimblehq.usecase.base.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class LoginByPasswordSingleUseCase @Inject constructor(
    rxSchedulerProvider: RxSchedulerProvider,
    private val authRepository: AuthRepository
) : SingleUseCase<LoginByPasswordSingleUseCase.Input, AuthData>(
    rxSchedulerProvider.io(),
    rxSchedulerProvider.main(),
    ::LoginError
) {

    data class Input(
        val email: String,
        val password: String
    )

    override fun create(input: Input): Single<AuthData> {
        return with(input) {
            authRepository.loginByPasswordWithEmail(
                email,
                password
            )
        }
    }
}
