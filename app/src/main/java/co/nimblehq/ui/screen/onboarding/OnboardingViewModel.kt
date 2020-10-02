package co.nimblehq.ui.screen.onboarding

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.ui.base.BaseViewModel

abstract class OnboardingViewModel : BaseViewModel() {

    abstract val inputs: Inputs

    interface Inputs { }
}

class OnboardingViewModelImpl @ViewModelInject constructor(
) : OnboardingViewModel(), OnboardingViewModel.Inputs {

    override val inputs: Inputs = this

    init { }
}
