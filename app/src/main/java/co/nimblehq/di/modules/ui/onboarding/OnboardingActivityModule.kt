package co.nimblehq.di.modules.ui.onboarding

import android.app.Activity
import co.nimblehq.ui.screen.onboarding.OnboardingNavigator
import co.nimblehq.ui.screen.onboarding.OnboardingNavigatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
class OnboardingActivityModule {

    @Provides
    fun provideOnboardingNavigator(activity: Activity): OnboardingNavigator = OnboardingNavigatorImpl(activity)
}
