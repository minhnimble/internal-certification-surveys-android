package co.nimblehq.ui.screen.onboarding.signin

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.ui.base.BaseViewModel

abstract class SignInViewModel : BaseViewModel() {

    abstract val firstInitialized: Boolean

    abstract val input: Input

    interface Input {
        fun updateInitialized(value: Boolean)
    }
}

class SignInViewModelImpl @ViewModelInject constructor(
) : SignInViewModel(), SignInViewModel.Input {

    private var _firstInitialized = true

    override val firstInitialized: Boolean
        get() = _firstInitialized

    override val input: Input
        get() = this

    override fun updateInitialized(value: Boolean) {
        _firstInitialized = value
    }
}
