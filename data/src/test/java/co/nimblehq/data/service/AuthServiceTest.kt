package co.nimblehq.data.service

import com.google.gson.Gson
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.data.lib.schedulers.RxSchedulerProviderImpl
import co.nimblehq.data.api.providers.ApiServiceProvider
import co.nimblehq.data.api.providers.ConverterFactoryProvider
import co.nimblehq.data.api.providers.RetrofitProvider
import co.nimblehq.data.api.service.survey.AuthService
import okhttp3.OkHttpClient
import org.junit.Assert
import org.junit.Test
import retrofit2.Retrofit

@Suppress("IllegalIdentifier")
class AuthServiceTest {

    @Test
    fun `API Service components should be initialize-able independently`() {
        val httpClient = OkHttpClient.Builder().build()
        val gson = Gson()
        val converterFactory = ConverterFactoryProvider.getConverterFactoryProvider(gson)
        val retrofitBuilder = RetrofitProvider.getRetrofitBuilder(converterFactory, httpClient)
        val appRetrofit: Retrofit = retrofitBuilder.build()

        val schedulers: RxSchedulerProvider = RxSchedulerProviderImpl()
        Assert.assertNotNull("should provide Retrofit", appRetrofit)

        val authService: AuthService = ApiServiceProvider.getAuthService(appRetrofit)
        Assert.assertNotNull("should provide AuthService", authService)
    }
}
