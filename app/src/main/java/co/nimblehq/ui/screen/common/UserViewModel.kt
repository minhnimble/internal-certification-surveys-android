package co.nimblehq.ui.screen.common

import co.nimblehq.data.model.User
import co.nimblehq.ui.base.BaseViewModel
import co.nimblehq.usecase.user.LoadCurrentUserInfoSingleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

interface Output {
    val error: Observable<Throwable>

    val user: Observable<User>
}

@HiltViewModel
class UserViewModel @Inject constructor(
    private val loadCurrentUserInfoSingleUseCase: LoadCurrentUserInfoSingleUseCase
) : BaseViewModel(), Output {

    val output = this

    private val _error = PublishSubject.create<Throwable>()
    override val error: Observable<Throwable>
        get() = _error

    private val _user = BehaviorSubject.create<User>()
    override val user: Observable<User>
        get() = _user

    init {
        loadCurrentUserInfo()
    }

    private fun loadCurrentUserInfo() {
        loadCurrentUserInfoSingleUseCase
            .execute(Unit)
            .subscribeBy(
                onError = { _error.onNext(it) },
                onSuccess = { _user.onNext(it) }
            ).bindForDisposable()
    }
}
