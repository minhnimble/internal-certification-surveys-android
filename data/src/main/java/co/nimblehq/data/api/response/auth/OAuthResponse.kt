package co.nimblehq.data.api.response.auth

import com.squareup.moshi.Json

data class OAuthResponse(
    @Json(name = "data") val data: OAuthDataResponse
)

data class OAuthDataResponse(
    @Json(name = "id") val id: String = "",
    @Json(name = "type") val type: String = "",
    @Json(name = "attributes") val attributes: OAuthAttributesResponse
)

data class OAuthAttributesResponse(
    @Json(name = "access_token") val accessToken: String = "",
    @Json(name = "refresh_token") val refreshToken: String = "",
    @Json(name = "created_at") val createdAt: Long = 0,
    @Json(name = "expires_in") val expiresIn: Long = 0,
    @Json(name = "token_type") val tokenType: String = ""
)
