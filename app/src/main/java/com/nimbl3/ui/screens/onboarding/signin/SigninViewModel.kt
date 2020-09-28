package com.nimbl3.ui.screens.onboarding.signin

import androidx.hilt.lifecycle.ViewModelInject
import com.nimbl3.ui.base.BaseViewModel

abstract class SigninViewModel : BaseViewModel() {

    interface Input { }
}

class SigninViewModelImpl @ViewModelInject constructor(
) : SigninViewModel(), SigninViewModel.Input { }
