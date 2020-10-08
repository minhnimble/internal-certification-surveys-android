package co.nimblehq.ui.screen.onboarding

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.data.lib.rxjava.RxBus
import co.nimblehq.event.NavigationEvent
import co.nimblehq.event.PostSessionCheckEvent
import co.nimblehq.ui.base.BaseViewModel
import co.nimblehq.usecase.session.GetUserTokenSingleUseCase
import co.nimblehq.usecase.session.RefreshTokenIfNeededSingleUseCase
import co.nimblehq.usecase.session.UpdateTokenCompletableUseCase
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject

class OnboardingViewModel @ViewModelInject constructor(
    private val getTokenSingleUseCase: GetUserTokenSingleUseCase,
    private val refreshTokenIfNeededSingleUseCase: RefreshTokenIfNeededSingleUseCase,
    private val updateTokenCompletableUseCase: UpdateTokenCompletableUseCase
) : BaseViewModel() {

    private val _error = PublishSubject.create<Throwable>()

    private val _navigator = PublishSubject.create<NavigationEvent>()

    val error: Observable<Throwable>
        get() = _error

    val navigator: Observable<NavigationEvent>
        get() = _navigator

    fun checkSession() {
        getTokenSingleUseCase
            .execute(Unit)
            .flatMap(refreshTokenIfNeededSingleUseCase::execute)
            .flatMapCompletable(updateTokenCompletableUseCase::execute)
            .subscribeBy(
                onError = {
                    _error.onNext(it)
                    RxBus.publish(PostSessionCheckEvent)
                },
                onComplete = { _navigator.onNext(NavigationEvent.Onboarding.Main) }
            ).bindForDisposable()
    }
}
