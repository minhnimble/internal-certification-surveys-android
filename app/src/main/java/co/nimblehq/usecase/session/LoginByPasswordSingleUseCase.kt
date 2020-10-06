package co.nimblehq.usecase.session

import co.nimblehq.data.error.LoginError
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.data.service.repository.auth.AuthRepository
import co.nimblehq.data.service.response.OAuthResponse
import co.nimblehq.usecase.base.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class LoginByPasswordSingleUseCase @Inject constructor(
    rxSchedulerProvider: RxSchedulerProvider,
    private val authRepository: AuthRepository
) : SingleUseCase<LoginByPasswordSingleUseCase.Input, OAuthResponse>(
    rxSchedulerProvider.io(),
    rxSchedulerProvider.main(),
    ::LoginError
) {

    data class Input(
        val email: String,
        val password: String
    )

    override fun create(input: Input): Single<OAuthResponse> {
        return with(input) {
            authRepository.loginByPasswordWithEmail(
                email,
                password
            )
        }
    }
}
