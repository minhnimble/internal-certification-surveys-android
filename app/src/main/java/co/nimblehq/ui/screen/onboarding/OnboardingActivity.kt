package co.nimblehq.ui.screen.onboarding

import android.os.Bundle
import androidx.activity.viewModels
import co.nimblehq.R
import co.nimblehq.extension.blurView
import co.nimblehq.ui.base.BaseActivity
import co.nimblehq.ui.screen.onboarding.signin.BlurAnimatable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_onboarding.*
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingActivity : BaseActivity(), BlurAnimatable {

    @Inject lateinit var navigator: OnboardingNavigator

    private val viewModel by viewModels<OnboardingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        bindViewModel()
        if (viewModel.checkSession().blockingGet()) {
            navigator.navigateToMainActivity()
        }
    }

    override fun animateBlurBackground() {
        clOnboardingBackground.blurView(
            shouldAnimate = true
        )
    }

    private fun bindViewModel() {
        viewModel.showServerError
            .subscribe { displayError(Throwable("Server error")) }
            .bindForDisposable()
    }
}
