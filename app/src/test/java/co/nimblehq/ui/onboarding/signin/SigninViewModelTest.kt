package co.nimblehq.ui.onboarding.signin

import co.nimblehq.data.error.LoginError
import co.nimblehq.ui.screens.onboarding.signin.SigninViewModel
import co.nimblehq.ui.screens.onboarding.signin.SigninViewModelImpl
import co.nimblehq.usecase.session.LoginByPasswordUseCase
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class SigninViewModelTest {

    private val email by lazy { "email" }
    private val password by lazy { "password" }

    private lateinit var mockLoginByPasswordUseCase: LoginByPasswordUseCase

    private lateinit var signingViewModel: SigninViewModel

    @Before
    fun setUp() {
        mockLoginByPasswordUseCase = mock()
        signingViewModel = SigninViewModelImpl(mockLoginByPasswordUseCase)
    }

    @Test
    fun `When input wrong email and password combination, it returns LoginError`() {
        whenever(
            mockLoginByPasswordUseCase.execute(any())
        ) doReturn Single.error<LoginError>(LoginError(null)).ignoreElement()

        val testSubscriber = signingViewModel
            .login(email, password)
            .test()

        // if error happens, we execute one more time in onErrorReturn, and if no condition is met,
        // onErrorReturn wraps error into CompositeException and throw it
        testSubscriber
            .assertError { error ->
                error is LoginError
            }
            .assertValueCount(0)
    }

    @Test
    fun `When input incorrect email format, Login button is disabled`() {
        val enableLoginButtonObserver = signingViewModel
            .enableLoginButton
            .test()

        signingViewModel.input.updateEmail(email)
        signingViewModel.input.updatePassword(password)

        enableLoginButtonObserver
            .assertNoErrors()
            .assertValueAt(0) {
                !it
            }
            .assertValueAt(1) {
                !it
            }
            .assertValueAt(2) {
                !it
            }
    }

    @Test
    fun `When input empty password, Login button is disabled`() {
        val enableLoginButtonObserver = signingViewModel
            .enableLoginButton
            .test()

        signingViewModel.input.updateEmail("minh@nimblehq.co")
        signingViewModel.input.updatePassword("")

        enableLoginButtonObserver
            .assertNoErrors()
            .assertValueAt(0) {
                !it
            }
            .assertValueAt(1) {
                !it
            }
            .assertValueAt(2) {
                !it
            }
    }

    @Test
    fun `When input valid email and password, Login button is enable`() {
        val enableLoginButtonObserver = signingViewModel
            .enableLoginButton
            .test()

        signingViewModel.input.updateEmail("minh@nimblehq.co")
        signingViewModel.input.updatePassword(password)

        enableLoginButtonObserver
            .assertNoErrors()
            .assertValueAt(0) {
                !it
            }
            .assertValueAt(1) {
                !it
            }
            .assertValueAt(2) {
                it
            }
    }

    @Test
    fun `When logging in completed, loading progress is gone`() {
        whenever(
            mockLoginByPasswordUseCase.execute(any())
        ) doReturn Completable.complete()

        signingViewModel
            .login(email, password)
            .test()

        val showLoadingObserver = signingViewModel.showLoading.test()

        showLoadingObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(false)
    }
}
