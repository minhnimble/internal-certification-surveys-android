package co.nimblehq.ui.screens.onboarding.signin

import androidx.fragment.app.viewModels
import co.nimblehq.R
import co.nimblehq.ui.base.BaseFragment
import co.nimblehq.ui.base.BaseFragmentCallbacks
import co.nimblehq.ui.screens.onboarding.OnboardingNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SigninFragment: BaseFragment(), BaseFragmentCallbacks {

    @Inject lateinit var navigator: OnboardingNavigator

    private val viewModel by viewModels<SigninViewModelImpl>()

    override val layoutRes = R.layout.fragment_signin

    override fun initViewModel() { }

    override fun setupView() { }

    override fun bindViewEvents() { }

    override fun bindViewModel() { }
}
