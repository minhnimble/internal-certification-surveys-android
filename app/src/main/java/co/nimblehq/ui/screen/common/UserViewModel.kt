package co.nimblehq.ui.screen.common

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.data.model.User
import co.nimblehq.ui.base.BaseViewModel
import co.nimblehq.usecase.user.LoadCurrentUserInfoSingleUseCase
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject

interface Output {
    val error: Observable<Throwable>

    val user: Observable<User>
}

class UserViewModel @ViewModelInject constructor(
    private val loadCurrentUserInfoSingleUseCase: LoadCurrentUserInfoSingleUseCase
) : BaseViewModel(), Output {

    val output = this

    private val _error = PublishSubject.create<Throwable>()
    override val error: Observable<Throwable>
        get() = _error

    private val _user = PublishSubject.create<User>()
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

