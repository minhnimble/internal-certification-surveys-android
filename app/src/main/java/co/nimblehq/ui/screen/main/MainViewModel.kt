package co.nimblehq.ui.screen.main

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.data.lib.rxjava.RxBus
import co.nimblehq.event.NavigationEvent
import co.nimblehq.event.PostSessionCheckEvent
import co.nimblehq.ui.base.BaseViewModel
import co.nimblehq.usecase.session.GetLocalUserTokenSingleUseCase
import co.nimblehq.usecase.session.RefreshTokenIfNeededSingleUseCase
import co.nimblehq.usecase.user.LoadCurrentUserInfoSingleUseCase
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject

class MainViewModel @ViewModelInject constructor(
    private val loadCurrentUserInfoSingleUseCase: LoadCurrentUserInfoSingleUseCase
) : BaseViewModel() {

    private val _error = PublishSubject.create<Throwable>()

    val error: Observable<Throwable>
        get() = _error

    fun loadCurrentUserInfo() {
        loadCurrentUserInfoSingleUseCase
            .execute(Unit)
            .subscribeBy(
                onError = { _error.onNext(it) },
                onSuccess = { RxBus.publish(it) }
            ).bindForDisposable()
    }
}
