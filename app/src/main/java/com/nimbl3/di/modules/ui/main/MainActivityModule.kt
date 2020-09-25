package com.nimbl3.di.modules.ui.main

import android.app.Activity
import com.nimbl3.ui.screens.main.MainNavigator
import com.nimbl3.ui.screens.main.MainNavigatorImpl
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
