package co.nimblehq.data.api.service.auth

import co.nimblehq.data.api.providers.ApiServiceProvider
import co.nimblehq.data.api.providers.ConverterFactoryProvider
import co.nimblehq.data.api.providers.MoshiBuilderProvider
import co.nimblehq.data.api.providers.RetrofitProvider
import okhttp3.OkHttpClient
import org.junit.Assert
import org.junit.Test
import retrofit2.Retrofit

@Suppress("IllegalIdentifier")
class AuthServiceTest {

    @Test
    fun `API Service components should be initialize-able independently`() {
        val httpClient = OkHttpClient.Builder().build()
        val moshi = MoshiBuilderProvider.moshiBuilder.build()
        val converterFactory = ConverterFactoryProvider.getMoshiConverterFactory(moshi)
        val retrofitBuilder = RetrofitProvider.getRetrofitBuilder(listOf(converterFactory), httpClient)

        val appRetrofit: Retrofit = retrofitBuilder.build()
        Assert.assertNotNull("should provide Retrofit", appRetrofit)

        val authService: AuthService = ApiServiceProvider.getAuthService(appRetrofit)
        Assert.assertNotNull("should provide AuthService", authService)
    }
}
