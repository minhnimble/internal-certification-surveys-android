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

    private val _loginError = PublishSubject.create<Throwable>()

    private val _showLoading = BehaviorSubject.create<Boolean>()

    private val _showMain = PublishSubject.create<Unit>()

    private var _firstInitialized = true

    val inputs: Inputs = this

    val enableLoginButton: Observable<Boolean>
        get() = Observables.combineLatest(
            _email,
            _password
        ) { email, password ->
            password.isNotEmpty() && email.isEmail()
        }

    val loginError: Observable<Throwable>
        get() = _loginError

    val showLoading: Observable<Boolean>
        get() = _showLoading

    val showMain: Observable<Unit>
        get() = _showMain

    val firstInitialized: Boolean
        get() = _firstInitialized

    fun login() {
        loginByPasswordSingleUseCase
            .execute(LoginByPasswordSingleUseCase.Input(_email.value.orEmpty(), _password.value.orEmpty()))
            .doOnSubscribe { _showLoading.onNext(true) }
            .map(UpdateTokenCompletableUseCase::Input)
            .flatMapCompletable(updateTokenCompletableUseCase::execute)
            .doFinally { _showLoading.onNext(false) }
            .subscribeBy(
                onComplete = { _showMain.onNext(Unit) },
                onError = { _loginError.onNext(it) }
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
