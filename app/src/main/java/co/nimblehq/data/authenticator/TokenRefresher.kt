package co.nimblehq.data.authenticator

import co.nimblehq.data.model.AuthData
import io.reactivex.Single

interface TokenRefresher {
    fun refreshToken(token: String): Single<AuthData>
}
