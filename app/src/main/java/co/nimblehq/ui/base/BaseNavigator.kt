package co.nimblehq.ui.base

import android.app.Activity
import androidx.annotation.IdRes
import androidx.navigation.NavController
import timber.log.Timber

interface BaseNavigator {

    fun findNavController(): NavController

    fun requestNavController(): NavController?

    fun navigateBack()
}

abstract class BaseNavigatorImpl(private val activity: Activity) : BaseNavigator {

    override fun requestNavController(): NavController? =
        try {
            findNavController()
        } catch (e: Exception) {
            Timber.e(e)
            null
        }

    override fun navigateBack() {
        findNavController().navigateUp()
    }

    private fun handleError(error: Throwable) {
        if (activity is BaseActivity) {
            Timber.e(error)
            activity.displayError(error)
        } else {
            throw error
        }
    }

    protected fun popBackTo(@IdRes destinationId: Int, inclusive: Boolean = false) {
        findNavController().popBackStack(destinationId, inclusive)
    }
}
