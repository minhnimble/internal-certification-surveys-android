package co.nimblehq.usecase.session

import co.nimblehq.data.error.LoginError
import co.nimblehq.data.lib.schedulers.TestRxSchedulerProviderImpl
import co.nimblehq.data.service.repository.auth.AuthRepository
import co.nimblehq.data.service.response.OAuthAttributesResponse
import co.nimblehq.data.service.response.OAuthDataResponse
import co.nimblehq.data.service.response.OAuthResponse
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.*

class LoginByPasswordSingleUseCaseTest {

    private lateinit var mockRepository: AuthRepository
    private lateinit var useCase: LoginByPasswordSingleUseCase

    @Before
    fun setUp() {
        mockRepository = mock()
        useCase = LoginByPasswordSingleUseCase(
            TestRxSchedulerProviderImpl(),
            mockRepository
        )
    }

    @Test
    fun `When logging in succeeds, it returns Complete`() {
        // Arrange
        whenever(
            mockRepository.loginByPasswordWithEmail(any(), any())
        ) doReturn Single.just(OAuthResponse(OAuthDataResponse("", "", OAuthAttributesResponse("","",0,"",0))))

        // Act
        val testSubscriber = useCase
            .execute(
                LoginByPasswordSingleUseCase.Input("email", "12345")
            )
            .test()

        // Assert
        testSubscriber
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun `When logging in fails, it returns LoginError`() {
        // Arrange
        whenever(
            mockRepository.loginByPasswordWithEmail(any(), any())
        ) doReturn  Single.error(LoginError(null))

        // Act
        val testNegativeSubscriber = useCase
            .execute(
                LoginByPasswordSingleUseCase.Input("email", "12345")
            )
            .test()

        // Assert
        testNegativeSubscriber
            .assertError { it is LoginError }
    }
}
