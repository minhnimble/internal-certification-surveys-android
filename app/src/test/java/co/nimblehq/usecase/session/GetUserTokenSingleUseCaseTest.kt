package co.nimblehq.usecase.session

import co.nimblehq.data.error.Ignored
import co.nimblehq.data.lib.schedulers.TestRxSchedulerProviderImpl
import co.nimblehq.data.storage.SecureStorage
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.*

class GetUserTokenSingleUseCaseTest {

    private lateinit var secureStorage: SecureStorage
    private lateinit var useCase: GetUserTokenSingleUseCase

    @Before
    fun setUp() {
        secureStorage = mock()
        useCase = GetUserTokenSingleUseCase(
            TestRxSchedulerProviderImpl(),
            secureStorage
        )
    }

    @Test
    fun `When stored token data is valid, it returns a AuthData value`() {
        // Arrange
        whenever(
            secureStorage.userAccessToken
        ) doReturn  "user access token"
        whenever(
            secureStorage.userRefreshToken
        ) doReturn  "user refresh token"
        whenever(
            secureStorage.userTokenType
        ) doReturn  "user token type"

        // Act
        val positiveTestSubscriber = useCase.execute(Unit).test()

        // Assert
        positiveTestSubscriber
            .assertNoErrors()
            .assertValueCount(1)
    }

    @Test
    fun `When stored token data is invalid, it returns a Ignored error`() {
        // Arrange
        whenever(
            secureStorage.userAccessToken
        ) doReturn  ""
        whenever(
            secureStorage.userRefreshToken
        ) doReturn  ""
        whenever(
            secureStorage.userTokenType
        ) doReturn  ""

        // Act
        val negativeTestSubscriber = useCase.execute(Unit).test()

        // Assert
        negativeTestSubscriber
            .assertValueCount(0)
            .assertError { it is Ignored }
    }
}
