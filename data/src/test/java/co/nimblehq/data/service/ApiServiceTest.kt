package co.nimblehq.data.service

import com.google.gson.Gson
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.data.lib.schedulers.RxSchedulerProviderImpl
import co.nimblehq.data.service.providers.ApiRepositoryProvider
import co.nimblehq.data.service.providers.ApiServiceProvider
import co.nimblehq.data.service.providers.ConverterFactoryProvider
import co.nimblehq.data.service.providers.RetrofitProvider
import co.nimblehq.data.service.repository.auth.AuthRepository
import co.nimblehq.data.service.repository.auth.AuthService
import co.nimblehq.data.storage.SecureStorage
import com.nhaarman.mockitokotlin2.mock
import okhttp3.OkHttpClient
import org.junit.Assert
import org.junit.Test
import retrofit2.Retrofit

@Suppress("IllegalIdentifier")
class ApiServiceTest {

    @Test
    fun `API Service components should be initialize-able independently`() {
        val httpClient = OkHttpClient.Builder().build()
        val gson = Gson()
        val converterFactory = ConverterFactoryProvider.getConverterFactoryProvider(gson)
        val retrofitBuilder = RetrofitProvider.getRetrofitBuilder(converterFactory, httpClient)
        val appRetrofit: Retrofit = retrofitBuilder.build()
        val secureStorage: SecureStorage = mock()

        val schedulers: RxSchedulerProvider = RxSchedulerProviderImpl()
        Assert.assertNotNull("should provide Retrofit", appRetrofit)

        val apiService: AuthService = ApiServiceProvider.getAuthService(appRetrofit)
        Assert.assertNotNull("should provide AuthService", apiService)

        val apiRepository: AuthRepository = ApiRepositoryProvider
            .getAuthRepository(apiService, secureStorage)
        Assert.assertNotNull("should provide ApiRepository", apiRepository)
    }
}
