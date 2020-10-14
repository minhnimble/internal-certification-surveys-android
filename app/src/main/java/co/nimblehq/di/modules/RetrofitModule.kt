package co.nimblehq.di.modules

import co.nimblehq.data.api.providers.ApiServiceProvider
import co.nimblehq.data.api.providers.ConverterFactoryProvider
import co.nimblehq.data.api.providers.RetrofitProvider
import co.nimblehq.data.api.service.auth.AuthService
import co.nimblehq.data.api.service.survey.SurveyService
import co.nimblehq.di.qualifier.AppOkHttpClient
import co.nimblehq.di.qualifier.AppRetrofit
import co.nimblehq.di.qualifier.AuthOkHttpClient
import co.nimblehq.di.qualifier.AuthRetrofit
import com.google.gson.Gson
import com.squareup.moshi.Moshi
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
    @AppRetrofit
    fun provideAppRetrofit(
        @AppOkHttpClient okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit {
        return RetrofitProvider
            .getRetrofitBuilder(converterFactory, okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @AuthRetrofit
    fun provideAuthRetrofit(
        @AuthOkHttpClient okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit {
        return RetrofitProvider
            .getRetrofitBuilder(converterFactory, okHttpClient)
            .build()
    }

    @Provides
    fun provideConverterFactory(moshi: Moshi): Converter.Factory = ConverterFactoryProvider.getMoshiConverterFactory(moshi)

    @Provides
    @Singleton
    fun provideAuthService(@AuthRetrofit retrofit: Retrofit): AuthService = ApiServiceProvider.getAuthService(retrofit)

    @Provides
    @Singleton
    fun provideSurveyService(@AppRetrofit retrofit: Retrofit): SurveyService = ApiServiceProvider.getSurveyService(retrofit)
}
