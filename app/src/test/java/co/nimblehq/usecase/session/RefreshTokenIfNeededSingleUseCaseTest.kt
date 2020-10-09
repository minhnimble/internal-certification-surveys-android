package co.nimblehq.usecase.session

import co.nimblehq.data.authenticator.TokenRefresher
import co.nimblehq.data.error.RefreshTokenError
import co.nimblehq.data.lib.schedulers.TestRxSchedulerProviderImpl
import co.nimblehq.data.model.AuthData
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class RefreshTokenIfNeededSingleUseCaseTest {

    private lateinit var mockTokenRefresher: TokenRefresher
    private lateinit var mockAuthData: AuthData
    private lateinit var useCase: RefreshTokenIfNeededSingleUseCase

    @Before
    fun setUp() {
        mockTokenRefresher = mock()
        mockAuthData = mock()
        useCase = RefreshTokenIfNeededSingleUseCase(
            TestRxSchedulerProviderImpl(),
            mockTokenRefresher
        )
    }

    @Test
    fun `When authData is expired and the refresh API call is successful, it returns a new AuthData`() {
        // Arrange
        whenever(
            mockAuthData.isExpired
        ) doReturn true
        whenever(
            mockTokenRefresher.refreshToken(mockAuthData.refreshToken)
        ) doReturn Single.just(
            AuthData(
                "",
                0,
                0,
                "",
                ""
            )
        )

        // Act
        val positiveTestSubscriber = useCase.execute(mockAuthData).test()

        // Assert
        positiveTestSubscriber
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue {
                it != mockAuthData
            }
    }

    @Test
    fun `When authData is expired and the refresh API call is successful, it returns a new RefreshTokenError`() {
        // Arrange
        whenever(
            mockAuthData.isExpired
        ) doReturn true
        whenever(
            mockTokenRefresher.refreshToken(mockAuthData.refreshToken)
        ) doReturn Single.error(RefreshTokenError())

        // Act
        val negativeTestSubscriber = useCase.execute(mockAuthData).test()

        // Assert
        negativeTestSubscriber
            .assertError { it is RefreshTokenError }
    }

    @Test
    fun `When authData is not expired, it returns the same AuthData`() {
        // Arrange
        whenever(
            mockAuthData.isExpired
        ) doReturn false
        whenever(
            mockTokenRefresher.refreshToken(mockAuthData.refreshToken)
        ) doReturn Single.just(mockAuthData)

        // Act
        val negativeTestSubscriber = useCase.execute(mockAuthData).test()

        // Assert
        negativeTestSubscriber
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { it == mockAuthData }
    }
}
