package co.nimblehq.data.service.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OAuthResponse(val data: OAuthDataResponse): Parcelable

@Parcelize
data class OAuthDataResponse(
        @SerializedName("id") val id: String,
        @SerializedName("type") val type: String,
        @SerializedName("attributes") val attributes: OAuthAttributesResponse
): Parcelable

@Parcelize
data class OAuthAttributesResponse(
        @SerializedName("access_token") val accessToken: String,
        @SerializedName("token_type") val tokenType: String,
        @SerializedName("expires_in")val expiresIn: Long,
        @SerializedName("refresh_token")val refreshToken: String,
        @SerializedName("created_at") val createdAt: Long
): Parcelable
