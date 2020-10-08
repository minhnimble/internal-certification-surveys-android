package co.nimblehq.ui.screen.onboarding.signin

import co.nimblehq.data.error.LoginError
import co.nimblehq.data.model.AuthData
import co.nimblehq.navigator.NavigationEvent
import co.nimblehq.usecase.session.LoginByPasswordSingleUseCase
import co.nimblehq.usecase.session.UpdateTokenCompletableUseCase
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
    private lateinit var mockUpdateTokenCompletableUseCase: UpdateTokenCompletableUseCase

    private lateinit var signingViewModel: SignInViewModel

    @Before
    fun setUp() {
        mockLoginByPasswordSingleUseCase = mock()
        mockUpdateTokenCompletableUseCase = mock()
        signingViewModel = SignInViewModel(mockLoginByPasswordSingleUseCase, mockUpdateTokenCompletableUseCase)
    }

    @Test
    fun `When input wrong email and password combination, first it will show the loading, then it returns LoginError and hides the loading when completed`() {
        // Arrange
        whenever(
            mockLoginByPasswordSingleUseCase.execute(any())
        ) doReturn Single.error(LoginError(null))

        // Act
        val showLoadingObserver = signingViewModel
            .showLoading
            .test()

        val signInErrorObserver = signingViewModel
            .signInError
            .test()

        signingViewModel.inputs.email("invalid@nimblehq.co")
        signingViewModel.inputs.password("12345678")
        signingViewModel.login()

        // Assert
        signInErrorObserver
            .assertNoErrors()
            .assertValue { it is LoginError }

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
    fun `When input valid email and password combination, it will open Main Activity`() {
        // Arrange
        whenever(
            mockLoginByPasswordSingleUseCase.execute(any())
        ) doReturn Single.just(AuthData("",0,0,"",""))
        whenever(
            mockUpdateTokenCompletableUseCase.execute(any())
        ) doReturn Completable.complete()

        // Act
        val navigatorObserver = signingViewModel
            .navigator
            .test()

        signingViewModel.inputs.email("valid@nimblehq.co")
        signingViewModel.inputs.password("12345678")
        signingViewModel.login()

        // Assert
        navigatorObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { it is NavigationEvent.SignIn.Main }
    }

    @Test
    fun `When input incorrect email format, Login button is disabled`() {
        // Arrange
        val enableLoginButtonObserver = signingViewModel
            .enableLoginButton
            .test()

        // Act
        signingViewModel.inputs.email("acbd")
        signingViewModel.inputs.password("12345678")

        // Assert
        enableLoginButtonObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(false)
    }

    @Test
    fun `When input empty password, Login button is disabled`() {
        // Arrange
        val enableLoginButtonObserver = signingViewModel
            .enableLoginButton
            .test()

        // Act
        signingViewModel.inputs.email("test@nimblehq.co")
        signingViewModel.inputs.password("")

        // Assert
        enableLoginButtonObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(false)
    }

    @Test
    fun `When input valid email and password, Login button is enabled`() {
        // Arrange
        val enableLoginButtonObserver = signingViewModel
            .enableLoginButton
            .test()

        // Act
        signingViewModel.inputs.email("test@nimblehq.co")
        signingViewModel.inputs.password("12345678")

        // Assert
        enableLoginButtonObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(true)
    }
}
