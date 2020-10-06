package co.nimblehq.di.modules

import com.google.gson.Gson
import co.nimblehq.data.api.interceptor.AppRequestInterceptor
import co.nimblehq.data.api.providers.ApiServiceProvider
import co.nimblehq.data.api.providers.ConverterFactoryProvider
import co.nimblehq.data.api.providers.RetrofitProvider
import co.nimblehq.data.repository.AuthRepository
import co.nimblehq.data.api.service.auth.AuthService
import co.nimblehq.data.provider.ApiRepositoryProvider
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

    @Provides
    @Singleton
    fun provideAuthRepository(authService: AuthService): AuthRepository = ApiRepositoryProvider.getAuthRepository(authService)

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService = ApiServiceProvider.getAuthService(retrofit)
}
