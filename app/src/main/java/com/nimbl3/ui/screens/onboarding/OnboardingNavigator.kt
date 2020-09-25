package com.nimbl3.ui.screens.onboarding

import android.app.Activity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.nimbl3.R
import com.nimbl3.ui.base.BaseNavigator
import com.nimbl3.ui.base.BaseNavigatorImpl
import javax.inject.Inject

interface OnboardingNavigator : BaseNavigator {

}

class OnboardingNavigatorImpl @Inject constructor(
        private val activity: Activity
) : BaseNavigatorImpl(activity), OnboardingNavigator {

    override fun findNavController(): NavController {
        return activity.findNavController(R.id.navHostContainer)
    }
}
