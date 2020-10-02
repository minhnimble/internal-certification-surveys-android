package co.nimblehq.usecase.session

import co.nimblehq.data.error.RefreshTokenError
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.data.model.AuthData
import co.nimblehq.data.repository.AuthRepository
import co.nimblehq.usecase.base.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class RefreshTokenIfNeededSingleUseCase @Inject constructor(
    rxSchedulerProvider: RxSchedulerProvider,
    private val authRepository: AuthRepository
) : SingleUseCase<RefreshTokenIfNeededSingleUseCase.Input, AuthData>(rxSchedulerProvider.io(), rxSchedulerProvider.io(), ::RefreshTokenError) {

    data class Input(
        val data: AuthData
    )

    override fun create(input: Input): Single<AuthData> {
        return with(input) {
            if (data.isExpired) {
                authRepository.refreshToken(
                    data.refreshToken
                )
            } else {
                Single.just(data)
            }
        }
    }
}
