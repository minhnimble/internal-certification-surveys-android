package co.nimblehq.ui.screen.onboarding

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.ui.base.BaseViewModel
import co.nimblehq.data.error.RefreshTokenError
import co.nimblehq.usecase.session.GetUserTokenSingleUseCase
import co.nimblehq.usecase.session.RefreshTokenIfNeededSingleUseCase
import co.nimblehq.usecase.session.UpdateTokenCompletableUseCase
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

class OnboardingViewModel @ViewModelInject constructor(
    private val getTokenSingleUseCase: GetUserTokenSingleUseCase,
    private val refreshTokenIfNeededSingleUseCase: RefreshTokenIfNeededSingleUseCase,
    private val updateTokenCompletableUseCase: UpdateTokenCompletableUseCase
) : BaseViewModel() {

    private val _refreshTokenError = BehaviorSubject.create<Throwable>()

    val showServerError: Observable<Unit>
        get() = _refreshTokenError
            .filter { error -> error is RefreshTokenError }
            .map { error -> error as RefreshTokenError }
            .flatMapMaybe { error ->
                if (error.hasInvalidGrantCode) Maybe.empty() else Maybe.just(Unit)
            }

    fun checkSession(): Single<Boolean> {
        return getTokenSingleUseCase
            .execute(Unit)
            .flatMap(refreshTokenIfNeededSingleUseCase::execute)
            .doOnError(_refreshTokenError::onNext)
            .flatMapCompletable(updateTokenCompletableUseCase::execute)
            .toSingleDefault(true)
            .onErrorReturnItem(false)
    }
}
