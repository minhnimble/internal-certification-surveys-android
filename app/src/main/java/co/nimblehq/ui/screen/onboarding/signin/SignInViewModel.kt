package co.nimblehq.ui.screen.onboarding.signin

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.ui.base.BaseViewModel

interface Inputs {
    fun updateInitialized(value: Boolean)
}

class SignInViewModel @ViewModelInject constructor(
) : BaseViewModel(), Inputs {

    private var _firstInitialized = true

    val firstInitialized: Boolean
        get() = _firstInitialized

    val inputs: Inputs = this

    override fun updateInitialized(value: Boolean) {
        _firstInitialized = value
    }
}


