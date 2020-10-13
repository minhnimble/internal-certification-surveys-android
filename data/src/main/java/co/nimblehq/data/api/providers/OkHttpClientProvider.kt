package co.nimblehq.data.api.providers

import co.nimblehq.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient

class OkHttpClientProvider {
    companion object {
        fun getOkHttpClient(
            apiRequestInterceptor: Interceptor,
            httpLoggingInterceptor: Interceptor
        ) : OkHttpClient {
            val httpClient = OkHttpClient.Builder().addInterceptor(apiRequestInterceptor)
            if (BuildConfig.DEBUG) {
                httpClient.addInterceptor(httpLoggingInterceptor)
            }
            return httpClient.build()
        }
    }
}
