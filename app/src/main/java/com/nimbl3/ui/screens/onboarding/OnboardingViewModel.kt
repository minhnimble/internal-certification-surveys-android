package com.nimbl3.ui.screens.onboarding

import androidx.hilt.lifecycle.ViewModelInject
import com.nimbl3.ui.base.BaseViewModel

abstract class OnboardingViewModel : BaseViewModel() {

    abstract val inputs: Inputs

    interface Inputs {
    }
}

class OnboardingViewModelImpl @ViewModelInject constructor(
) : OnboardingViewModel(), OnboardingViewModel.Inputs {

    override val inputs: Inputs = this

    init {

    }
}
