package com.nimbl3.di.modules

import com.google.gson.Gson
import com.nimbl3.data.service.interceptor.AppRequestInterceptor
import com.nimbl3.data.service.providers.ConverterFactoryProvider
import com.nimbl3.data.service.providers.RetrofitProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun provideApiRetrofit(okHttpClient: OkHttpClient, converterFactory: Converter.Factory): Retrofit {
        return RetrofitProvider
            .getRetrofitBuilder(converterFactory, okHttpClient)
            .build()
    }

    @Provides
    fun provideConverterFactory(gson: Gson): Converter.Factory {
        return ConverterFactoryProvider.getConverterFactoryProvider(gson)
    }

    @Provides
    fun provideAppRequestInterceptor(): AppRequestInterceptor = AppRequestInterceptor()
}