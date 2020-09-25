package com.nimbl3.ui.screens.onboarding.signin

import androidx.fragment.app.viewModels
import com.nimbl3.R
import com.nimbl3.ui.base.BaseFragment
import com.nimbl3.ui.base.BaseFragmentCallbacks
import com.nimbl3.ui.screens.onboarding.OnboardingNavigator
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
