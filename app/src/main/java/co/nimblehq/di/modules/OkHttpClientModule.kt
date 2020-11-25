package co.nimblehq.di.modules

import co.nimblehq.data.api.authenticator.AppRequestAuthenticator
import co.nimblehq.data.api.interceptor.AppRequestInterceptor
import co.nimblehq.data.api.interceptor.AuthRequestInterceptor
import co.nimblehq.data.api.providers.OkHttpClientProvider
import co.nimblehq.data.api.service.auth.AuthService
import co.nimblehq.data.storage.SecureStorage
import co.nimblehq.di.qualifier.AppOkHttpClient
import co.nimblehq.di.qualifier.AuthOkHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class OkHttpClientModule {

    @Provides
    @Singleton
    @AppOkHttpClient
    fun provideAppOkHttpClient(
        apiRequestInterceptor: AppRequestInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        apiRequestAuthenticator: Authenticator
    ): OkHttpClient {
        return OkHttpClientProvider.getOkHttpClient(apiRequestInterceptor, httpLoggingInterceptor, apiRequestAuthenticator)
    }

    @Provides
    @Singleton
    @AuthOkHttpClient
    fun provideAuthOkHttpClient(
        authRequestInterceptor: AuthRequestInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClientProvider.getOkHttpClient(authRequestInterceptor, httpLoggingInterceptor)
    }

    @Provides
    @Singleton
    fun provideAppRequestInterceptor(
        securedStorage: SecureStorage
    ): AppRequestInterceptor {
        return AppRequestInterceptor(securedStorage)
    }

    @Provides
    @Singleton
    fun provideAuthRequestInterceptor(): AuthRequestInterceptor {
        return AuthRequestInterceptor()
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Provides
    @Singleton
    fun provideAppRequestAuthenticator(
        securedStorage: SecureStorage,
        authService: AuthService
    ): Authenticator {
        return AppRequestAuthenticator(securedStorage, authService)
    }
}
