package co.nimblehq.di.modules

import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.data.lib.schedulers.RxSchedulerProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@Module
class SchedulersModule {

    @Provides
    fun provideSchedulersProvider(): RxSchedulerProvider = RxSchedulerProviderImpl()
}
