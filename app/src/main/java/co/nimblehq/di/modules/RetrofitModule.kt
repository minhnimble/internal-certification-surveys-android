package co.nimblehq.di.modules

import co.nimblehq.data.api.providers.ApiServiceProvider
import co.nimblehq.data.api.providers.ConverterFactoryProvider
import co.nimblehq.data.api.providers.RetrofitProvider
import co.nimblehq.data.api.service.auth.AuthService
import co.nimblehq.data.api.service.survey.SurveyService
import co.nimblehq.data.api.service.user.UserService
import co.nimblehq.di.qualifier.*
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
        @JsonApiConverterFactory jsonApiConverterFactory: Converter.Factory,
        @MoshiConverterFactory moshiConverterFactory: Converter.Factory
    ): Retrofit {
        return RetrofitProvider
            .getRetrofitBuilder(listOf(jsonApiConverterFactory, moshiConverterFactory), okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @AuthRetrofit
    fun provideAuthRetrofit(
        @AuthOkHttpClient okHttpClient: OkHttpClient,
        @JsonApiConverterFactory jsonApiConverterFactory: Converter.Factory,
        @MoshiConverterFactory moshiConverterFactory: Converter.Factory
        ): Retrofit {
        return RetrofitProvider
            .getRetrofitBuilder(listOf(jsonApiConverterFactory, moshiConverterFactory), okHttpClient)
            .build()
    }

    @Provides
    @MoshiConverterFactory
    fun provideMoshiConverterFactory(moshi: Moshi): Converter.Factory = ConverterFactoryProvider.getMoshiConverterFactory(moshi)

    @Provides
    @JsonApiConverterFactory
    fun provideJsonApiConverterFactory(moshi: Moshi): Converter.Factory = ConverterFactoryProvider.getJsonApiConverterFactory(moshi)

    @Provides
    @Singleton
    fun provideAuthService(@AuthRetrofit retrofit: Retrofit): AuthService = ApiServiceProvider.getAuthService(retrofit)

    @Provides
    @Singleton
    fun provideSurveyService(@AppRetrofit retrofit: Retrofit): SurveyService = ApiServiceProvider.getSurveyService(retrofit)

    @Provides
    @Singleton
    fun provideUserService(@AppRetrofit retrofit: Retrofit): UserService = ApiServiceProvider.getUserService(retrofit)
}
