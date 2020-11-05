package co.nimblehq.usecase.user

import co.nimblehq.data.error.UserError
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.data.model.User
import co.nimblehq.data.repository.UserRepository
import co.nimblehq.usecase.base.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class LoadCurrentUserInfoSingleUseCase @Inject constructor(
    rxSchedulerProvider: RxSchedulerProvider,
    private val userRepository: UserRepository
) : SingleUseCase<Unit, User>(
    rxSchedulerProvider.io(),
    rxSchedulerProvider.main(),
    UserError::LoadCurrentUserInfoError
) {

    override fun create(input: Unit): Single<User> {
        return userRepository.loadCurrentUserInfo()
    }
}
