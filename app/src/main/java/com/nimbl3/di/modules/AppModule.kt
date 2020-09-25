package com.nimbl3.di.modules

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Qualifier

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    // TODO: Prepare sample annotations for later usage
    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class RemoteSurveysDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalSurveysDataSource
}