package co.nimblehq.data.repository

import co.nimblehq.data.api.operators.Operators
import co.nimblehq.data.api.service.user.UserService
import co.nimblehq.data.model.User
import co.nimblehq.data.model.toUser
import io.reactivex.Single
import javax.inject.Inject

interface UserRepository {

    fun loadCurrentUserInfo(): Single<User>
}

class UserRepositoryImpl @Inject constructor(
    private val userService: UserService
) : UserRepository {

    override fun loadCurrentUserInfo(): Single<User> {
        return userService
            .loadCurrentUserInfo()
            .lift(Operators.apiError())
            .firstOrError()
            .map { it.toUser() }
    }
}
