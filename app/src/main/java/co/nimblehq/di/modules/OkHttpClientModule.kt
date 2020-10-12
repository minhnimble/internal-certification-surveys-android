package co.nimblehq.di.modules

import co.nimblehq.data.api.interceptor.AppRequestInterceptor
import co.nimblehq.data.api.interceptor.AuthRequestInterceptor
import co.nimblehq.data.api.providers.OkHttpClientProvider
import co.nimblehq.data.storage.SecureStorage
import co.nimblehq.di.qualifier.AppOkHttpClient
import co.nimblehq.di.qualifier.AuthOkHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@InstallIn(ApplicationComponent::class)
@Module
class OkHttpClientModule {

    @Provides
    @AppOkHttpClient
    fun provideAppOkHttpClient(
        apiRequestInterceptor: AppRequestInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClientProvider.getApiOkHttpClient(apiRequestInterceptor, httpLoggingInterceptor)
    }

    @Provides
    @AuthOkHttpClient
    fun provideAuthOkHttpClient(
        authRequestInterceptor: AuthRequestInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClientProvider.getApiOkHttpClient(authRequestInterceptor, httpLoggingInterceptor)
    }

    @Provides
    fun provideAppRequestInterceptor(
        securedStorage: SecureStorage
    ): AppRequestInterceptor {
        return AppRequestInterceptor(securedStorage)
    }

    @Provides
    fun provideAuthRequestInterceptor(): AuthRequestInterceptor {
        return AuthRequestInterceptor()
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }
}
