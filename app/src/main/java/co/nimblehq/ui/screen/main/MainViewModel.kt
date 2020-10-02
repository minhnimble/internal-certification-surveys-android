package co.nimblehq.ui.screen.main

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.ui.base.BaseViewModel
import co.nimblehq.usecase.session.GetUserTokenSingleUseCase
import co.nimblehq.usecase.session.RefreshTokenIfNeededSingleUseCase
import co.nimblehq.usecase.session.UpdateTokenCompletableUseCase
import io.reactivex.Single

class MainViewModel @ViewModelInject constructor(
    private val getTokenSingleUseCase: GetUserTokenSingleUseCase,
    private val refreshTokenIfNeededSingleUseCase: RefreshTokenIfNeededSingleUseCase,
    private val updateTokenCompletableUseCase: UpdateTokenCompletableUseCase
) : BaseViewModel() {
    fun checkSession(): Single<Boolean> {
        return getTokenSingleUseCase
            .execute(Unit)
            .flatMap { authData ->
                refreshTokenIfNeededSingleUseCase
                    .execute(RefreshTokenIfNeededSingleUseCase.Input(authData))
            }
            .map(UpdateTokenCompletableUseCase::Input)
            .flatMapCompletable(updateTokenCompletableUseCase::execute)
            .toSingleDefault(true)
            .onErrorReturnItem(false)
    }
}
