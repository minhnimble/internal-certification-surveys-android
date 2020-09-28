package co.nimblehq.di.modules.ui.main

import android.app.Activity
import co.nimblehq.ui.screens.main.MainNavigator
import co.nimblehq.ui.screens.main.MainNavigatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
class MainActivityModule {

    @Provides
    fun provideMainNavigator(activity: Activity): MainNavigator = MainNavigatorImpl(activity)

}
