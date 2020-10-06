package co.nimblehq.ui.screen.onboarding.signin

import co.nimblehq.data.error.Ignored
import co.nimblehq.data.error.LoginError
import co.nimblehq.data.service.response.OAuthAttributesResponse
import co.nimblehq.data.service.response.OAuthDataResponse
import co.nimblehq.data.service.response.OAuthResponse
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
    fun `When input wrong email and password combination, it returns LoginError`() {
        // Arrange
        whenever(
            mockLoginByPasswordSingleUseCase.execute(any())
        ) doReturn Single.error(LoginError(null))

        // Act
        val isLoginSuccessObserver = signingViewModel
            .isLoginSuccess
            .test()
        signingViewModel.inputs.email("invalid@nimblehq.co")
        signingViewModel.inputs.password("12345678")
        signingViewModel.login()

        // Assert
        isLoginSuccessObserver
            .assertNoErrors()
            .assertValue { it is LoginError }
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
    fun `When input valid email and password, Login button is enable`() {
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

    @Test
    fun `When logging in completed, loading progress is gone`() {
        // Arrange
        whenever(
            mockLoginByPasswordSingleUseCase.execute(any())
        ) doReturn Single.just(OAuthResponse(OAuthDataResponse("", "", OAuthAttributesResponse("","",0,"",0))))

        // Act
        signingViewModel.inputs.email("test@nimblehq.co")
        signingViewModel.inputs.password("12345678")
        signingViewModel.login()

        val showLoadingObserver = signingViewModel.showLoading.test()

        showLoadingObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(false)
    }
}
