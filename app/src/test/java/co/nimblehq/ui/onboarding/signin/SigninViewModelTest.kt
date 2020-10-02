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

    private lateinit var mockLoginByPasswordUseCase: LoginByPasswordUseCase

    private lateinit var signingViewModel: SigninViewModel

    @Before
    fun setUp() {
        mockLoginByPasswordUseCase = mock()
        signingViewModel = SigninViewModelImpl(mockLoginByPasswordUseCase)
    }

    @Test
    fun `When input wrong email and password combination, it returns LoginError`() {
        // Arrange
        whenever(
            mockLoginByPasswordUseCase.execute(any())
        ) doReturn Single.error<LoginError>(LoginError(null)).ignoreElement()

        // Act
        val testSubscriber = signingViewModel
            .login("invalid@nimblehq.co", "12345678")
            .test()

        // Assert
        testSubscriber
            .assertError { error ->
                error is LoginError
            }
            .assertValueCount(0)
    }

    @Test
    fun `When input incorrect email format, Login button is disabled`() {
        // Arrange
        val enableLoginButtonObserver = signingViewModel
            .enableLoginButton
            .test()

        // Act
        signingViewModel.input.updateEmail("acbd")
        signingViewModel.input.updatePassword("12345678")

        // Assert
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
        // Arrange
        val enableLoginButtonObserver = signingViewModel
            .enableLoginButton
            .test()

        // Act
        signingViewModel.input.updateEmail("test@nimblehq.co")
        signingViewModel.input.updatePassword("")

        // Assert
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
        // Arrange
        val enableLoginButtonObserver = signingViewModel
            .enableLoginButton
            .test()

        // Act
        signingViewModel.input.updateEmail("test@nimblehq.co")
        signingViewModel.input.updatePassword("12345678")

        // Assert
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
        // Arrange
        whenever(
            mockLoginByPasswordUseCase.execute(any())
        ) doReturn Completable.complete()

        // Act
        signingViewModel
            .login("test@nimblehq.co", "12345678")
            .test()

        val showLoadingObserver = signingViewModel.showLoading.test()

        showLoadingObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(false)
    }
}
