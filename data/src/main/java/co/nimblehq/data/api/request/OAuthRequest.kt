package co.nimblehq.data.api.request

import com.squareup.moshi.Json

sealed class OAuthRequest {

	data class LoginByPasswordWithEmail(
        @Json(name = "grant_type") val grantType: String,
        @Json(name = "email") val email: String,
        @Json(name = "password") val password: String,
        @Json(name = "client_id") val clientId: String,
        @Json(name = "client_secret") val clientSecret: String
	) : OAuthRequest()

	data class RefreshToken(
		@Json(name = "grant_type") val grantType: String,
		@Json(name = "refresh_token") val refreshToken: String,
		@Json(name = "client_id") val clientId: String,
		@Json(name = "client_secret") val clientSecret: String
	) : OAuthRequest()
}
