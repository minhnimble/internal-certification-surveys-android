package co.nimblehq.data.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthRequestInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        return chain.proceed(request.build())
    }
}
