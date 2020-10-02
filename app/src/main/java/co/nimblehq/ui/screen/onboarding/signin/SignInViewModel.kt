package co.nimblehq.ui.screen.onboarding.signin

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.ui.base.BaseViewModel
import co.nimblehq.extension.isEmail
import co.nimblehq.usecase.session.LoginByPasswordCompletableUseCase
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.BehaviorSubject

interface Inputs {
    fun updateEmail(email: String)
    fun updateInitialized(value: Boolean)
    fun updatePassword(password: String)
}

class SignInViewModel @ViewModelInject constructor(
    private val loginByPasswordCompletableUseCase: LoginByPasswordCompletableUseCase
) : BaseViewModel(), Inputs {

    private val _email = BehaviorSubject.createDefault("")

    private var _firstInitialized = true

    private val _password = BehaviorSubject.createDefault("")

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

    val showLoading: Observable<Boolean>
        get() = _showLoading

    fun login(
        email: String,
        password: String
    ): Completable {
        return loginByPasswordCompletableUseCase
            .execute(LoginByPasswordCompletableUseCase.Input(email, password))
            .doOnSubscribe { _showLoading.onNext(true) }
            .doOnError {
                _showLoading.onNext(false)
            }
            .doOnComplete {
                _showLoading.onNext(false)
            }
    }

    override fun updateEmail(email: String) {
        _email.onNext(email)
    }

    override fun updateInitialized(value: Boolean) {
        _firstInitialized = value
    }

    override fun updatePassword(password: String) {
        _password.onNext(password)
    }
}
