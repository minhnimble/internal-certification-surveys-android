package co.nimblehq.ui.screen.onboarding.signin

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import co.nimblehq.R
import co.nimblehq.data.error.LoginError
import co.nimblehq.data.lib.extension.subscribeOnClick
import co.nimblehq.extension.animateResource
import co.nimblehq.extension.startFadeInAnimation
import co.nimblehq.ui.base.BaseFragment
import co.nimblehq.ui.base.BaseFragmentCallbacks
import co.nimblehq.ui.screen.onboarding.OnboardingNavigator
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxkotlin.subscribeBy
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
            viewModel.inputs.initialized(false)
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

    override fun bindViewEvents() {
        btSignInLogin.subscribeOnClick {
            viewModel.login()
        }.bindForDisposable()

        etSignInEmail.addTextChangedListener {
            viewModel.inputs.email(it.toString())
        }

        etSignInPassword.addTextChangedListener {
            viewModel.inputs.password(it.toString())
        }
    }

    override fun bindViewModel() {
        viewModel.enableLoginButton
            .subscribe(::bindEnableLoginBtn)
            .bindForDisposable()

        viewModel.isLoginSuccess
            .subscribe(::bindLoginStatus)
            .bindForDisposable()

        viewModel.showLoading
            .subscribe(::bindLoading)
            .bindForDisposable()
    }

    private fun bindEnableLoginBtn(isEnabled: Boolean) {
        btSignInLogin.isEnabled = isEnabled
    }

    private fun bindLoading(isLoading: Boolean) {
        toggleLoading(isLoading)
    }

    private fun bindLoginStatus(error: Throwable) {
        when (error) {
            is LoginError -> { displayError(error) }
            else -> { navigator.navigateToMainActivity() }
        }
    }
}
