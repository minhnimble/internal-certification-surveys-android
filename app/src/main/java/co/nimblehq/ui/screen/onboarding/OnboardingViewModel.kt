package co.nimblehq.ui.screen.onboarding

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.ui.base.BaseViewModel
import co.nimblehq.usecase.session.GetUserTokenSingleUseCase
import co.nimblehq.usecase.session.RefreshTokenIfNeededSingleUseCase
import co.nimblehq.usecase.session.UpdateTokenCompletableUseCase
import io.reactivex.Completable

class OnboardingViewModel @ViewModelInject constructor(
    private val getTokenSingleUseCase: GetUserTokenSingleUseCase,
    private val refreshTokenIfNeededSingleUseCase: RefreshTokenIfNeededSingleUseCase,
    private val updateTokenCompletableUseCase: UpdateTokenCompletableUseCase
) : BaseViewModel() {


    fun checkSession(): Completable {
        return getTokenSingleUseCase
            .execute(Unit)
            .flatMap(refreshTokenIfNeededSingleUseCase::execute)
            .flatMapCompletable(updateTokenCompletableUseCase::execute)
    }
}
