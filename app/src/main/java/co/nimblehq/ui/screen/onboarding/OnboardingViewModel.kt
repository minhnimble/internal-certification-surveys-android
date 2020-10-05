package co.nimblehq.ui.screen.onboarding

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.ui.base.BaseViewModel

interface Inputs { }

class OnboardingViewModel @ViewModelInject constructor(
) : BaseViewModel(), Inputs {

    val inputs: Inputs = this

    init { }
}
