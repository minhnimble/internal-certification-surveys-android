package co.nimblehq.ui.screens.onboarding

import android.app.Activity
import android.content.Intent
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.findNavController
import co.nimblehq.R
import co.nimblehq.ui.base.BaseNavigator
import co.nimblehq.ui.base.BaseNavigatorImpl
import co.nimblehq.ui.screens.main.MainActivity
import javax.inject.Inject

interface OnboardingNavigator : BaseNavigator {
    fun navigateToMainActivity()
}

class OnboardingNavigatorImpl @Inject constructor(
        private val activity: Activity
) : BaseNavigatorImpl(activity), OnboardingNavigator {

    override fun findNavController(): NavController {
        return activity.findNavController(R.id.navHostContainer)
    }

    override fun navigateToMainActivity() {
        val activityNavigator = ActivityNavigator(activity)
        activityNavigator.navigate(
            activityNavigator.createDestination().setIntent(
                Intent(
                    activity,
                    MainActivity::class.java
                )
            ), null, null, null
        )
        activity.finish()
    }
}
