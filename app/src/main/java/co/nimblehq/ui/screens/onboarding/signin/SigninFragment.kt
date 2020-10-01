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

@AndroidEntryPoint
class SigninFragment: BaseFragment(), BaseFragmentCallbacks {

    @Inject lateinit var navigator: OnboardingNavigator

    private var blurAnimator: BlurAnimatable? = null

    private val viewModel by viewModels<SigninViewModelImpl>()

    override val layoutRes = R.layout.fragment_signin

    override fun initViewModel() { }

    override fun setupView() {
        if (viewModel.firstInitialized) {
            ivNimbleLogo.startFadeInAnimation {
                // Animate to show blur image on background of the current fragment's activity if it conforms BlurAnimatable
                blurAnimator?.animateBlurBackground()

                // Animate to move Nimble title logo up
                clSignin.moveResourceToCenterTop(R.id.ivNimbleLogo)

                // Animate to show user signin inputs
                btForgotPassword.startFadeInAnimation()
                llInputContents.startFadeInAnimation()
            }
            viewModel.input.updateInitialized(false)
        } else {
            ivNimbleLogo.startFadeInAnimation(shouldAnimate = false)
            clSignin.moveResourceToCenterTop(resId = R.id.ivNimbleLogo, shouldAnimate = false)
            btForgotPassword.startFadeInAnimation(shouldAnimate = false)
            llInputContents.startFadeInAnimation(shouldAnimate = false)
        }
    }

    override fun bindViewEvents() { }

    override fun bindViewModel() { }

    fun setBlurAnimator(blurAnimator: BlurAnimatable) {
        this.blurAnimator = blurAnimator
    }

    interface BlurAnimatable {
        fun animateBlurBackground()
    }
}
