package co.nimblehq.ui.screen.onboarding.signin

import androidx.fragment.app.viewModels
import co.nimblehq.R
import co.nimblehq.extension.animateResource
import co.nimblehq.extension.startFadeInAnimation
import co.nimblehq.ui.base.BaseFragment
import co.nimblehq.ui.base.BaseFragmentCallbacks
import co.nimblehq.ui.screen.onboarding.OnboardingNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_sign_in.*
import javax.inject.Inject

interface BlurAnimatable {
    fun animateBlurBackground()
}

@AndroidEntryPoint
class SignInFragment: BaseFragment(), BaseFragmentCallbacks {

    @Inject lateinit var navigator: OnboardingNavigator

    private val viewModel by viewModels<SignInViewModel>()

    override val layoutRes = R.layout.fragment_sign_in

    override fun initViewModel() { }

    override fun setupView() {
        if (viewModel.firstInitialized) {
            ivSignInNimbleLogo.startFadeInAnimation {
                // Animate to show blur image on background of the current fragment's activity if it conforms BlurAnimatable
                (activity as? BlurAnimatable)?.animateBlurBackground()

                // Animate to move Nimble title logo to above Sign in inputs view
                clSignIn.animateResource(
                    resId = R.id.ivSignInNimbleLogo,
                    toTopOfResId = R.id.llSignInInputContainer
                )

                // Animate to show user sign in inputs
                 btSignInForgotPassword.startFadeInAnimation()
                llSignInInputContainer.startFadeInAnimation()
            }
            viewModel.inputs.updateInitialized(false)
        } else {
            ivSignInNimbleLogo.startFadeInAnimation(shouldAnimate = false)
            clSignIn.animateResource(
                resId = R.id.ivSignInNimbleLogo,
                toTopOfResId = R.id.llSignInInputContainer,
                shouldAnimate = false
            )
            btSignInForgotPassword.startFadeInAnimation(shouldAnimate = false)
            llSignInInputContainer.startFadeInAnimation(shouldAnimate = false)
        }
    }

    override fun bindViewEvents() { }

    override fun bindViewModel() { }
}
