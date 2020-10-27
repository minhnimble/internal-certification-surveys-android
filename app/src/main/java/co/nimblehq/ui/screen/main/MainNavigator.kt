package co.nimblehq.ui.screen.main

import android.app.Activity
import android.content.Intent
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.findNavController
import co.nimblehq.R
import co.nimblehq.ui.base.BaseNavigator
import co.nimblehq.ui.base.BaseNavigatorImpl
import co.nimblehq.ui.screen.main.surveys.SurveyItemUiModel
import co.nimblehq.ui.screen.onboarding.OnboardingActivity
import co.nimblehq.ui.screen.main.surveys.SurveysFragmentDirections.Companion.actionSurveysFragmentToSurveyDetailsFragment
import javax.inject.Inject

interface MainNavigator : BaseNavigator {
    fun navigateToOnboardingActivity()

    fun navigateToSurveyDetails(surveyItemUiModel: SurveyItemUiModel)
}

class MainNavigatorImpl @Inject constructor(
    private val activity: Activity
) : BaseNavigatorImpl(activity), MainNavigator {

    override fun findNavController(): NavController {
        return activity.findNavController(R.id.fcvMainNavHostContainer)
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
        activity.finish()
    }

    override fun navigateToSurveyDetails(surveyItemUiModel: SurveyItemUiModel) {
        val navController = findNavController()
        when (navController.graph.id) {
            R.id.main_nav_graph -> {
                when (navController.currentDestination?.id) {
                    R.id.surveysFragment -> {
                        val action = actionSurveysFragmentToSurveyDetailsFragment(surveyItemUiModel)
                        navController.navigate(action)
                    }
                    else -> unsupportedNavigation()
                }
            }
            else -> unsupportedNavigation()
        }
    }
}
