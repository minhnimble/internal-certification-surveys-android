package co.nimblehq.ui.screens.main

import android.app.Activity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import co.nimblehq.R
import co.nimblehq.ui.base.BaseNavigator
import co.nimblehq.ui.base.BaseNavigatorImpl
import javax.inject.Inject

interface MainNavigator : BaseNavigator {

}

class MainNavigatorImpl @Inject constructor(
    private val activity: Activity
) : BaseNavigatorImpl(activity), MainNavigator {

    override fun findNavController(): NavController {
        return activity.findNavController(R.id.navHostContainer)
    }
}
