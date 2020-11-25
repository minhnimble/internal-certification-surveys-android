package co.nimblehq.data.model

import co.nimblehq.data.api.response.user.UserResponse

data class User(
    var id: String,
    var email: String,
    var avatarUrl: String
)

fun UserResponse.toUser() = User(
    id = id.orEmpty(),
    email = email.orEmpty(),
    avatarUrl = avatarUrl.orEmpty()
)
