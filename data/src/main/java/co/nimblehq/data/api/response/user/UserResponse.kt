package co.nimblehq.data.api.response.user

import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource

@JsonApi(type = "user")
data class UserResponse(
    @field: Json(name = "email") var email: String? = null,
    @field: Json(name = "avatar_url") var avatarUrl: String? = null
) : Resource()
