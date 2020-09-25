package com.nimbl3.di.modules

import com.nimbl3.data.lib.schedulers.RxScheduler.MainThread
import com.nimbl3.data.lib.schedulers.RxScheduler.IoThread
import com.nimbl3.data.lib.schedulers.RxSchedulerProvider
import com.nimbl3.data.lib.schedulers.RxSchedulerProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@Module
class SchedulersModule {

    @Provides
    fun provideIoThread(): IoThread = IoThread

    @Provides
    fun provideMainThread(): MainThread = MainThread

    @Provides
    fun provideSchedulersProvider(): RxSchedulerProvider = RxSchedulerProviderImpl()
}