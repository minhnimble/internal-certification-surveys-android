package co.nimblehq.ui.screen.onboarding.signin

import co.nimblehq.event.NavigationEvent
import co.nimblehq.extension.isEmail
import co.nimblehq.ui.base.BaseViewModel
import co.nimblehq.usecase.session.LoginByPasswordSingleUseCase
import co.nimblehq.usecase.session.UpdateLocalUserTokenCompletableUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

interface Input {

    fun email(input: String)

    fun password(input: String)

    fun initialized(input: Boolean)
}

interface Output {

    val signInError: Observable<Throwable>

    val showLoading: Observable<Boolean>

    val navigator: Observable<NavigationEvent>

    val firstInitialized: Boolean

    val enableLoginButton: Observable<Boolean>
}

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val loginByPasswordSingleUseCase: LoginByPasswordSingleUseCase,
    private val updateLocalUserTokenCompletableUseCase: UpdateLocalUserTokenCompletableUseCase
) : BaseViewModel(), Input, Output {

    val input = this

    val output = this

    private val _email = BehaviorSubject.create<String>()
    private val _password = BehaviorSubject.create<String>()
    override val enableLoginButton: Observable<Boolean>
        get() = Observables.combineLatest(
            _email,
            _password
        ) { email, password ->
            password.isNotEmpty() && email.isEmail()
        }

    private val _signInError = PublishSubject.create<Throwable>()
    override val signInError: Observable<Throwable>
        get() = _signInError

    private val _showLoading = BehaviorSubject.create<Boolean>()
    override val showLoading: Observable<Boolean>
        get() = _showLoading

    private val _navigator = PublishSubject.create<NavigationEvent>()
    override val navigator: Observable<NavigationEvent>
        get() = _navigator

    private var _firstInitialized = true
    override val firstInitialized: Boolean
        get() = _firstInitialized

    fun login() {
        loginByPasswordSingleUseCase
            .execute(LoginByPasswordSingleUseCase.Input(_email.value.orEmpty(), _password.value.orEmpty()))
            .doOnSubscribe { _showLoading.onNext(true) }
            .flatMapCompletable(updateLocalUserTokenCompletableUseCase::execute)
            .doFinally { _showLoading.onNext(false) }
            .subscribeBy(
                onComplete = { _navigator.onNext(NavigationEvent.SignIn.Main) },
                onError = { _signInError.onNext(it) }
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
