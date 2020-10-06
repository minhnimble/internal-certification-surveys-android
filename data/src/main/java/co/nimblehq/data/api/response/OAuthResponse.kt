package co.nimblehq.data.api.response

import com.google.gson.annotations.SerializedName

data class OAuthResponse(
	@SerializedName("data")val data: OAuthDataResponse
)

data class OAuthDataResponse(
        @SerializedName("id") val id: String,
        @SerializedName("type") val type: String,
        @SerializedName("attributes") val attributes: OAuthAttributesResponse
)

data class OAuthAttributesResponse(
        @SerializedName("access_token") val accessToken: String,
        @SerializedName("token_type") val tokenType: String,
        @SerializedName("expires_in")val expiresIn: Long,
        @SerializedName("refresh_token")val refreshToken: String,
        @SerializedName("created_at") val createdAt: Long
)
