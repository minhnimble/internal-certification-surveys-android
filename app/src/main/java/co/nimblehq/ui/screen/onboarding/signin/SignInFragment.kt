package co.nimblehq.ui.screen.onboarding.signin

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import co.nimblehq.R
import co.nimblehq.data.lib.extension.subscribeOnClick
import co.nimblehq.data.lib.rxjava.RxBus
import co.nimblehq.event.NavigationEvent
import co.nimblehq.event.PostSessionCheckEvent
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

    override fun setupView() { }

    override fun bindViewEvents() {
        btSignInLogin
            .subscribeOnClick(viewModel::login)
            .bindForDisposable()

        etSignInEmail.addTextChangedListener {
            viewModel.inputs.email(it.toString())
        }

        etSignInPassword.addTextChangedListener {
            viewModel.inputs.password(it.toString())
        }

        RxBus.listen(PostSessionCheckEvent::class.java)
            .subscribe { executePostSessionCheck() }
            .bindForDisposable()
    }

    override fun bindViewModel() {
        viewModel.enableLoginButton
            .subscribe(::bindEnableLoginButton)
            .bindForDisposable()

        viewModel.signInError
            .subscribe(::displayError)
            .bindForDisposable()

        viewModel.showLoading
            .subscribe(::bindLoading)
            .bindForDisposable()

        viewModel.navigator
            .subscribe {
                when (it) {
                    is NavigationEvent.SignIn.Main -> showMainActivity()
                }
            }
            .bindForDisposable()
    }

    private fun executePostSessionCheck() {
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
            ivSignInNimbleLogo.startFadeInAnimation(0)
            clSignIn.animateResource(
                resId = R.id.ivSignInNimbleLogo,
                toTopOfResId = R.id.llSignInInputContainer,
                shouldAnimate = false
            )
            btSignInForgotPassword.startFadeInAnimation(0)
            llSignInInputContainer.startFadeInAnimation(0)
        }
    }

    private fun bindEnableLoginButton(isEnabled: Boolean) {
        btSignInLogin.isEnabled = isEnabled
    }

    private fun bindLoading(isLoading: Boolean) {
        toggleLoading(isLoading)
    }

    private fun showMainActivity() {
        navigator.navigateToMainActivity()
    }
}
