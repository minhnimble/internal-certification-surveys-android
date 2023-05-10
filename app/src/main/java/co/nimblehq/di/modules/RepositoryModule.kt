package co.nimblehq.di.modules

import co.nimblehq.data.api.service.auth.AuthService
import co.nimblehq.data.api.service.survey.SurveyService
import co.nimblehq.data.api.service.user.UserService
import co.nimblehq.data.authenticator.TokenRefresher
import co.nimblehq.data.repository.*
import co.nimblehq.data.storage.AppPreferences
import co.nimblehq.data.storage.dao.SurveyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(authService: AuthService): AuthRepository = AuthRepositoryImpl(authService)

    @Provides
    @Singleton
    fun provideSurveyRepository(appPreferences: AppPreferences, surveyDao: SurveyDao, surveyService: SurveyService): SurveyRepository = SurveyRepositoryImpl(appPreferences, surveyDao, surveyService)

    @Provides
    fun provideTokenRefresher(authRepository: AuthRepository): TokenRefresher = authRepository

    @Provides
    @Singleton
    fun provideUserRepository(userService: UserService): UserRepository = UserRepositoryImpl(userService)
}
