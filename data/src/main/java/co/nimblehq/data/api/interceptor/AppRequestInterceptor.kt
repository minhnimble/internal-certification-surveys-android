package co.nimblehq.data.api.interceptor

import co.nimblehq.data.lib.common.AUTHORIZATION_HEADER
import co.nimblehq.data.storage.SecureStorage
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class AppRequestInterceptor @Inject constructor(
    private val securedStorage: SecureStorage
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val tokenType = securedStorage.userTokenType
        val token = securedStorage.userAccessToken
        request.addHeader(AUTHORIZATION_HEADER, "$tokenType $token")
        return chain.proceed(request.build())
    }
}
