package co.nimblehq.data.storage

import android.content.SharedPreferences
import androidx.core.content.edit
import co.nimblehq.data.crypto.AESCrypto
import co.nimblehq.data.crypto.Obfuscator
import co.nimblehq.data.crypto.SecretKeyException
import javax.inject.Inject

interface SecureStorage {
    var userAccessToken: String?
    var userAccessTokenCreatedAt: Long?
    var userAccessTokenExpiresIn: Long?
    var userRefreshToken: String?
    var userTokenType: String?
}

private const val KEY_USER_ACCESS_TOKEN = "user_access_token"
private const val KEY_USER_ACCESS_TOKEN_CREATED_AT = "user_access_token_created_at"
private const val KEY_USER_ACCESS_TOKEN_EXPIRES_IN = "user_access_token_expires_in"
private const val KEY_USER_REFRESH_TOKEN = "user_refresh_token"
private const val KEY_USER_TOKEN_TYPE = "user_token_type"

class SecureStorageImpl @Inject constructor(
    private val preferences: SharedPreferences,
    private val crypto: AESCrypto
) : SecureStorage {

    override var userAccessToken: String?
        get() = get(KEY_USER_ACCESS_TOKEN)
        set(accessToken) = setOrRemove(KEY_USER_ACCESS_TOKEN, accessToken)

    override var userAccessTokenCreatedAt: Long?
        get() = get(KEY_USER_ACCESS_TOKEN_CREATED_AT)?.toLong()
        set(createdAt) = setOrRemove(KEY_USER_ACCESS_TOKEN_CREATED_AT, createdAt?.toString())

    override var userAccessTokenExpiresIn: Long?
        get() = get(KEY_USER_ACCESS_TOKEN_EXPIRES_IN)?.toLong()
        set(expiresIn) = setOrRemove(KEY_USER_ACCESS_TOKEN_EXPIRES_IN, expiresIn?.toString())

    override var userRefreshToken: String?
        get() = get(KEY_USER_REFRESH_TOKEN)
        set(refreshToken) = setOrRemove(KEY_USER_REFRESH_TOKEN, refreshToken)

    override var userTokenType: String?
        get() = get(KEY_USER_TOKEN_TYPE)
        set(tokenType) = setOrRemove(KEY_USER_TOKEN_TYPE, tokenType)

    private fun get(key: String): String? {
        var value = preferences.getString(key, null)
        if (value != null) {
            value = try {
                crypto.decrypt(value, Obfuscator.reveal())
            } catch (e: SecretKeyException) {
                // Remove the item from preferences as it cannot be decrypted any more
                setOrRemove(key, null)
                null
            }
        }
        return value
    }

    private fun setOrRemove(key: String, value: String?) {
        preferences.edit {
            if (value == null) {
                remove(key)
            } else {
                putString(key, crypto.encrypt(value, Obfuscator.reveal()))
            }
        }
    }
}
