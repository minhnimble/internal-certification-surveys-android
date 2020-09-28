package co.nimblehq.ui.screens.main

import android.app.Activity
import android.content.Intent
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.findNavController
import co.nimblehq.R
import co.nimblehq.ui.base.BaseNavigator
import co.nimblehq.ui.base.BaseNavigatorImpl
import co.nimblehq.ui.screens.onboarding.OnboardingActivity
import javax.inject.Inject

interface MainNavigator : BaseNavigator {
    fun navigateToOnboardingActivity()
}

class MainNavigatorImpl @Inject constructor(
    private val activity: Activity
) : BaseNavigatorImpl(activity), MainNavigator {

    override fun findNavController(): NavController {
        return activity.findNavController(R.id.navHostContainer)
    }

    override fun navigateToOnboardingActivity() {
        val activityNavigator = ActivityNavigator(activity)
        activityNavigator.navigate(
            activityNavigator.createDestination().setIntent(
                Intent(
                    activity,
                    OnboardingActivity::class.java
                )
            ), null, null, null
        )
    }
}
