package co.nimblehq.ui.screen.onboarding

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import co.nimblehq.R
import co.nimblehq.data.error.RefreshTokenError
import co.nimblehq.data.error.TokenExpiredError
import co.nimblehq.data.lib.rxjava.RxBus
import co.nimblehq.event.PostSessionCheckEvent
import co.nimblehq.extension.blurView
import co.nimblehq.ui.base.BaseActivity
import co.nimblehq.ui.screen.onboarding.signin.BlurAnimatable
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_onboarding.*
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingActivity : BaseActivity(), BlurAnimatable {

    @Inject lateinit var navigator: OnboardingNavigator

    private val viewModel by viewModels<OnboardingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_onboarding)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        viewModel.checkSession().subscribeBy(
            onError = {
                when (it) {
                    is RefreshTokenError -> displayError(TokenExpiredError(null))
                }
                RxBus.publish(PostSessionCheckEvent)
            },
            onComplete = { navigator.navigateToMainActivity() }
        ).bindForDisposable()
    }

    override fun animateBlurBackground() {
        clOnboardingBackground.blurView(
            shouldAnimate = true
        )
    }
}
