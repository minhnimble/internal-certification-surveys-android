package co.nimblehq.ui.screen.onboarding.signin

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.data.error.Ignored
import co.nimblehq.ui.base.BaseViewModel
import co.nimblehq.extension.isEmail
import co.nimblehq.usecase.session.LoginByPasswordSingleUseCase
import co.nimblehq.usecase.session.UpdateTokenCompletableUseCase
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface Inputs {
    fun email(input: String)

    fun password(input: String)

    fun initialized(input: Boolean)
}

class SignInViewModel @ViewModelInject constructor(
    private val loginByPasswordSingleUseCase: LoginByPasswordSingleUseCase,
    private val updateTokenCompletableUseCase: UpdateTokenCompletableUseCase
) : BaseViewModel(), Inputs {

    private val _email = BehaviorSubject.create<String>()

    private val _password = BehaviorSubject.create<String>()

    private var _firstInitialized = true

    private val _loginStatus = PublishSubject.create<Throwable>()

    private val _showLoading = BehaviorSubject.create<Boolean>()

    val inputs: Inputs = this

    val enableLoginButton: Observable<Boolean>
        get() = Observables.combineLatest(
            _email,
            _password
        ) { email, password ->
            password.isNotEmpty() && email.isEmail()
        }

    val firstInitialized: Boolean
        get() = _firstInitialized

    val loginStatus: Observable<Throwable>
        get() = _loginStatus

    val showLoading: Observable<Boolean>
        get() = _showLoading

    fun login() {
        loginByPasswordSingleUseCase
            .execute(LoginByPasswordSingleUseCase.Input(_email.value.orEmpty(), _password.value.orEmpty()))
            .doOnSubscribe { _showLoading.onNext(true) }
            .map(UpdateTokenCompletableUseCase::Input)
            .flatMapCompletable(updateTokenCompletableUseCase::execute)
            .doFinally { _showLoading.onNext(false) }
            .subscribeBy(
                onComplete = { _loginStatus.onNext(Ignored(null)) }, // TODO: Use a navigator instead of _loginStatus Observable
                onError = { _loginStatus.onNext(it) }
            )
            .bindForDisposable()
    }

    override fun email(input: String) {
        _email.onNext(input)
    }

    override fun password(input: String) {
        _password.onNext(input)
    }

    override fun initialized(input: Boolean) {
        _firstInitialized = input
    }
}
