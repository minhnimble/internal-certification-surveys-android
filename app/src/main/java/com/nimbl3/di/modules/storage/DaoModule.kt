package com.nimbl3.di.modules.storage

import com.nimbl3.data.storage.SurveyDatabase
import com.nimbl3.data.storage.dao.SurveyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class DaoModule {

    @Provides
    @Singleton
    internal fun surveyDao(db: SurveyDatabase): SurveyDao {
        return db.surveyDao()
    }
}
