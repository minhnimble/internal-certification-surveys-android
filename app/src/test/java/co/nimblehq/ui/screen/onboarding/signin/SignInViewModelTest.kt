package co.nimblehq.ui.screen.onboarding.signin

import co.nimblehq.data.error.SessionError
import co.nimblehq.data.model.AuthData
import co.nimblehq.event.NavigationEvent
import co.nimblehq.usecase.session.LoginByPasswordSingleUseCase
import co.nimblehq.usecase.session.UpdateLocalUserTokenCompletableUseCase
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class SignInViewModelTest {

    private lateinit var mockLoginByPasswordSingleUseCase: LoginByPasswordSingleUseCase
    private lateinit var mockUpdateLocalUserTokenCompletableUseCase: UpdateLocalUserTokenCompletableUseCase

    private lateinit var signingViewModel: SignInViewModel

    @Before
    fun setUp() {
        mockLoginByPasswordSingleUseCase = mock()
        mockUpdateLocalUserTokenCompletableUseCase = mock()
        signingViewModel = SignInViewModel(mockLoginByPasswordSingleUseCase, mockUpdateLocalUserTokenCompletableUseCase)
    }

    @Test
    fun `When providing a wrong email and password combination, first it shows the loading, then returns a LoginError and hides the loading when completed`() {
        // Arrange
        whenever(
            mockLoginByPasswordSingleUseCase.execute(any())
        ) doReturn Single.error(SessionError.LoginError(null))

        // Act
        val showLoadingObserver = signingViewModel
            .showLoading
            .test()

        val signInErrorObserver = signingViewModel
            .signInError
            .test()

        signingViewModel.input.email("invalid@nimblehq.co")
        signingViewModel.input.password("12345678")
        signingViewModel.login()

        // Assert
        signInErrorObserver
            .assertNoErrors()
            .assertValue { it is SessionError.LoginError }

        showLoadingObserver
            .assertNoErrors()
            .assertValueAt(0) {
                it
            }
            .assertValueAt(1) {
                !it
            }
    }

    @Test
    fun `When providing a valid email and password combination, it opens Main Activity`() {
        // Arrange
        whenever(
            mockLoginByPasswordSingleUseCase.execute(any())
        ) doReturn Single.just(AuthData())
        whenever(
            mockUpdateLocalUserTokenCompletableUseCase.execute(any())
        ) doReturn Completable.complete()

        // Act
        val navigatorObserver = signingViewModel
            .navigator
            .test()

        signingViewModel.input.email("valid@nimblehq.co")
        signingViewModel.input.password("12345678")
        signingViewModel.login()

        // Assert
        navigatorObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { it is NavigationEvent.SignIn.Main }
    }

    @Test
    fun `When providing an email with incorrect format, Login button is disabled`() {
        // Arrange
        val enableLoginButtonObserver = signingViewModel
            .enableLoginButton
            .test()

        // Act
        signingViewModel.input.email("acbd")
        signingViewModel.input.password("12345678")

        // Assert
        enableLoginButtonObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(false)
    }

    @Test
    fun `When providing an empty password, Login button is disabled`() {
        // Arrange
        val enableLoginButtonObserver = signingViewModel
            .enableLoginButton
            .test()

        // Act
        signingViewModel.input.email("test@nimblehq.co")
        signingViewModel.input.password("")

        // Assert
        enableLoginButtonObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(false)
    }

    @Test
    fun `When providing a valid email and password combination, Login button is enabled`() {
        // Arrange
        val enableLoginButtonObserver = signingViewModel
            .enableLoginButton
            .test()

        // Act
        signingViewModel.input.email("test@nimblehq.co")
        signingViewModel.input.password("12345678")

        // Assert
        enableLoginButtonObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(true)
    }
}
