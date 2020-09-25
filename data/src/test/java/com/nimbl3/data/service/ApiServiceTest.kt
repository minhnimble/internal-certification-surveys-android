package com.nimbl3.data.service

import com.google.gson.Gson
import com.nimbl3.data.lib.schedulers.RxSchedulerProvider
import com.nimbl3.data.lib.schedulers.RxSchedulerProviderImpl
import com.nimbl3.data.service.providers.ConverterFactoryProvider
import com.nimbl3.data.service.providers.RetrofitProvider
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


        val schedulers: RxSchedulerProvider = RxSchedulerProviderImpl()
        Assert.assertNotNull("should provide Retrofit", appRetrofit)

        // TODO: Write test for providing Services and Repositories
    }
}