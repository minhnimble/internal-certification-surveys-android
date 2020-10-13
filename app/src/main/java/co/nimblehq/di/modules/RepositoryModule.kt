package co.nimblehq.di.modules

import co.nimblehq.data.api.service.auth.AuthService
import co.nimblehq.data.api.service.survey.SurveyService
import co.nimblehq.data.authenticator.TokenRefresher
import co.nimblehq.data.repository.AuthRepository
import co.nimblehq.data.repository.AuthRepositoryImpl
import co.nimblehq.data.repository.SurveyRepository
import co.nimblehq.data.repository.SurveyRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(authService: AuthService): AuthRepository = AuthRepositoryImpl(authService)

    @Provides
    @Singleton
    fun provideSurveyRepository(surveyService: SurveyService): SurveyRepository = SurveyRepositoryImpl(surveyService)

    @Provides
    fun provideTokenRefresher(authRepository: AuthRepository): TokenRefresher = authRepository
}

