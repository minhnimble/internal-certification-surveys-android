package co.nimblehq.ui.screen.onboarding.signin

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import co.nimblehq.R
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

    private val viewModel by viewModels<SignInViewModelImpl>()

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
            viewModel.input.updateInitialized(false)
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
            viewModel.login(
                email = etSignInEmail.text.toString(),
                password = etSignInPassword.text.toString()
            ).subscribeBy(
                onComplete = :: openMainActivity,
                onError = ::handleError
            ).bindForDisposable()
        }.bindForDisposable()

        etSignInEmail.addTextChangedListener {
            viewModel.input.updateEmail(it.toString())
        }

        etSignInPassword.addTextChangedListener {
            viewModel.input.updatePassword(it.toString())
        }
    }

    override fun bindViewModel() {
        viewModel.enableLoginButton
            .subscribeBy { btSignInLogin.isEnabled = it }
            .bindForDisposable()

        viewModel.showLoading
            .subscribe(::bindLoading)
            .bindForDisposable()
    }

    private fun bindLoading(isLoading: Boolean) {
        toggleLoading(isLoading)
    }

    private fun handleError(error: Throwable) {
        displayError(error)
    }

    private fun openMainActivity() {
        navigator.navigateToMainActivity()
    }
}
