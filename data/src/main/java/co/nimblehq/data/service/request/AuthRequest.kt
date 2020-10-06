package co.nimblehq.data.service.request

import com.google.gson.annotations.SerializedName

data class LoginByPasswordWithEmailRequest(
    @SerializedName("grant_type") val grantType: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("client_id") val clientId: String,
    @SerializedName("client_secret") val clientSecret: String
)
