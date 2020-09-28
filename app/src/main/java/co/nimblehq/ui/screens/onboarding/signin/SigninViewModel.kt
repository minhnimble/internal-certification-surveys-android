package co.nimblehq.ui.screens.onboarding.signin

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.ui.base.BaseViewModel

abstract class SigninViewModel : BaseViewModel() {

    interface Input { }
}

class SigninViewModelImpl @ViewModelInject constructor(
) : SigninViewModel(), SigninViewModel.Input { }
