package co.nimblehq.ui.screen.onboarding

import android.os.Bundle
import androidx.activity.viewModels
import co.nimblehq.R
import co.nimblehq.data.error.RefreshTokenError
import co.nimblehq.data.error.TokenExpiredError
import co.nimblehq.event.NavigationEvent
import co.nimblehq.extension.blurView
import co.nimblehq.ui.base.BaseActivity
import co.nimblehq.ui.screen.onboarding.signin.BlurAnimatable
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_onboarding.*
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingActivity : BaseActivity(), BlurAnimatable {

    @Inject
    lateinit var navigator: OnboardingNavigator

    private val viewModel by viewModels<OnboardingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_onboarding)
        bindViewModel()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        viewModel.checkSession()
    }

    override fun animateBlurBackground() {
        clOnboardingBackground.blurView(
            shouldAnimate = true
        )
    }

    private fun bindViewModel() {
        viewModel.output.error
            .subscribeBy {
                when (it) {
                    is RefreshTokenError -> displayError(TokenExpiredError(null))
                }
            }
            .bindForDisposable()

        viewModel.output.navigator
            .subscribeBy {
                when (it) {
                    is NavigationEvent.Onboarding.Main -> showMainActivity()
                }
            }
            .bindForDisposable()
    }

    private fun showMainActivity() {
        navigator.navigateToMainActivity()
    }
}
