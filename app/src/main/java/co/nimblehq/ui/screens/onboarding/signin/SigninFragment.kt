package co.nimblehq.ui.screens.onboarding.signin

import androidx.fragment.app.viewModels
import co.nimblehq.R
import co.nimblehq.extension.moveResourceToCenterTop
import co.nimblehq.extension.startFadeInAnimation
import co.nimblehq.ui.base.BaseFragment
import co.nimblehq.ui.base.BaseFragmentCallbacks
import co.nimblehq.ui.screens.onboarding.OnboardingNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_signin.*
import javax.inject.Inject

interface BlurAnimatable {
    fun animateBlurBackground()
}

@AndroidEntryPoint
class SigninFragment: BaseFragment(), BaseFragmentCallbacks {

    @Inject lateinit var navigator: OnboardingNavigator

    private val viewModel by viewModels<SigninViewModelImpl>()

    override val layoutRes = R.layout.fragment_signin

    override fun initViewModel() { }

    override fun setupView() {
        if (viewModel.firstInitialized) {
            ivSigninNimbleLogo.startFadeInAnimation {
                // Animate to show blur image on background of the current fragment's activity if it conforms BlurAnimatable
                (activity as? BlurAnimatable)?.animateBlurBackground()

                // Animate to move Nimble title logo up
                clSignin.moveResourceToCenterTop(R.id.ivSigninNimbleLogo)

                // Animate to show user signin inputs
                btForgotPassword.startFadeInAnimation()
                llSigninInputContainer.startFadeInAnimation()
            }
            viewModel.input.updateInitialized(false)
        } else {
            ivSigninNimbleLogo.startFadeInAnimation(shouldAnimate = false)
            clSignin.moveResourceToCenterTop(resId = R.id.ivSigninNimbleLogo, shouldAnimate = false)
            btForgotPassword.startFadeInAnimation(shouldAnimate = false)
            llSigninInputContainer.startFadeInAnimation(shouldAnimate = false)
        }
    }

    override fun bindViewEvents() { }

    override fun bindViewModel() { }
}
