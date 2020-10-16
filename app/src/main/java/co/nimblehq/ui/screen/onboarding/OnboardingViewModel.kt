package co.nimblehq.ui.screen.onboarding

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.data.lib.rxjava.RxBus
import co.nimblehq.event.NavigationEvent
import co.nimblehq.event.PostSessionCheckEvent
import co.nimblehq.ui.base.BaseViewModel
import co.nimblehq.usecase.session.GetLocalUserTokenSingleUseCase
import co.nimblehq.usecase.session.RefreshTokenIfNeededSingleUseCase
import co.nimblehq.usecase.session.UpdateLocalUserTokenCompletableUseCase
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject

class OnboardingViewModel @ViewModelInject constructor(
    private val getLocalTokenSingleUseCase: GetLocalUserTokenSingleUseCase,
    private val refreshTokenIfNeededSingleUseCase: RefreshTokenIfNeededSingleUseCase,
    private val updateLocalUserTokenCompletableUseCase: UpdateLocalUserTokenCompletableUseCase
) : BaseViewModel() {

    private val _error = PublishSubject.create<Throwable>()

    private val _navigator = PublishSubject.create<NavigationEvent>()

    val error: Observable<Throwable>
        get() = _error

    val navigator: Observable<NavigationEvent>
        get() = _navigator

    fun checkSession() {
        getLocalTokenSingleUseCase
            .execute(Unit)
            .flatMap(refreshTokenIfNeededSingleUseCase::execute)
            .flatMapCompletable(updateLocalUserTokenCompletableUseCase::execute)
            .subscribeBy(
                onError = {
                    _error.onNext(it)
                    RxBus.publish(PostSessionCheckEvent)
                },
                onComplete = { _navigator.onNext(NavigationEvent.Onboarding.Main) }
            ).bindForDisposable()
    }
}
