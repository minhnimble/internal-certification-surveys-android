package co.nimblehq.ui.screen.onboarding

import co.nimblehq.data.error.RefreshTokenError
import co.nimblehq.data.model.AuthData
import co.nimblehq.usecase.session.GetUserTokenSingleUseCase
import co.nimblehq.usecase.session.RefreshTokenIfNeededSingleUseCase
import co.nimblehq.usecase.session.UpdateTokenCompletableUseCase
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

@Suppress("IllegalIdentifier")
class OnboardingViewModelTest {

    private lateinit var mockGetUserTokenSingleUseCase: GetUserTokenSingleUseCase
    private lateinit var mockRefreshTokenIfNeededSingleUseCase: RefreshTokenIfNeededSingleUseCase
    private lateinit var mockUpdateTokenCompletableUseCase: UpdateTokenCompletableUseCase

    private lateinit var onboardingViewModel: OnboardingViewModel

    @Before
    fun setUp() {
        mockGetUserTokenSingleUseCase = mock()
        mockRefreshTokenIfNeededSingleUseCase = mock()
        mockUpdateTokenCompletableUseCase = mock()
        onboardingViewModel = OnboardingViewModel(
            mockGetUserTokenSingleUseCase,
            mockRefreshTokenIfNeededSingleUseCase,
            mockUpdateTokenCompletableUseCase
        )
    }

    @Test
    fun `When session has expired token but unable to refresh token, it will return checkSession as false, and serverError will trigger`() {
        // Arrange
        whenever(
            mockGetUserTokenSingleUseCase.execute(any())
        ) doReturn Single.just(AuthData("abc",1000,100,"cde","password"))
        whenever(
            mockRefreshTokenIfNeededSingleUseCase.execute(any())
        ) doReturn Single.error(RefreshTokenError())

        // Act
        val showServerErrorObserver = onboardingViewModel
            .showServerError
            .test()
        val checkSessionObserver = onboardingViewModel
            .checkSession()
            .test()

        // Assert
        checkSessionObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(false)

        showServerErrorObserver
            .assertNoErrors()
            .assertValue(Unit)
    }

    @Test
    fun `When session has invalid token it will return checkSession as false, but no trigger serverError`() {
        // Arrange
        whenever(
            mockGetUserTokenSingleUseCase.execute(any())
        ) doReturn Maybe.empty<AuthData>().toSingle()

        // Act
        val showServerErrorObserver = onboardingViewModel
            .showServerError
            .test()
        val checkSessionObserver = onboardingViewModel
            .checkSession()
            .test()

        // Assert
        checkSessionObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(false)

        showServerErrorObserver
            .assertNoErrors()
            .assertValueCount(0)
    }

    @Test
    fun `When session has valid token, it will return checkSession as true`() {
        // Arrange
        val validAuthData = AuthData(
            "abcderfasdas",
            1234,
            7200,
            "abcderqweq",
            "password"
        )
        whenever(
            mockGetUserTokenSingleUseCase.execute(any())
        ) doReturn Single.just(validAuthData)
        whenever(
            mockRefreshTokenIfNeededSingleUseCase.execute(any())
        ) doReturn Single.just(validAuthData)
        whenever(
            mockUpdateTokenCompletableUseCase.execute(any())
        ) doReturn Completable.complete()

        // Act
        val checkSessionObserver = onboardingViewModel
            .checkSession()
            .test()

        // Assert
        checkSessionObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(true)
    }
}
