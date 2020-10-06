package co.nimblehq.data.model

import co.nimblehq.data.api.response.OAuthResponse

data class AuthData(
	val accessToken: String,
	val tokenType: String,
	val expiresIn: Long,
	val refreshToken: String,
	val createdAt: Long
)

fun OAuthResponse.toAuthData() = AuthData(
	this.data.attributes.accessToken,
	this.data.attributes.tokenType,
	this.data.attributes.expiresIn,
	this.data.attributes.refreshToken,
	this.data.attributes.createdAt
)
