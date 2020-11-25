package co.nimblehq.data.api.providers

import co.nimblehq.data.BuildConfig
import co.nimblehq.data.lib.common.DEFAULT_API_TIMEOUT_IN_SEC
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class OkHttpClientProvider {
    companion object {
        fun getOkHttpClient(
            apiRequestInterceptor: Interceptor,
            httpLoggingInterceptor: Interceptor,
            appRequestAuthenticator: Authenticator? = null
        ) : OkHttpClient {
            val httpClient = OkHttpClient.Builder()
                .connectTimeout(DEFAULT_API_TIMEOUT_IN_SEC, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_API_TIMEOUT_IN_SEC, TimeUnit.SECONDS)
                .addInterceptor(apiRequestInterceptor)
            appRequestAuthenticator?.let { httpClient.authenticator(it) }
            if (BuildConfig.DEBUG) {
                httpClient.addInterceptor(httpLoggingInterceptor)
            }
            return httpClient.build()
        }
    }
}
