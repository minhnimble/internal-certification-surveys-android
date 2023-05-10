package co.nimblehq.di.modules.storage

import co.nimblehq.data.storage.SurveyDatabase
import co.nimblehq.data.storage.dao.SurveyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DaoModule {

    @Provides
    @Singleton
    internal fun surveyDao(db: SurveyDatabase): SurveyDao {
        return db.surveyDao()
    }
}
