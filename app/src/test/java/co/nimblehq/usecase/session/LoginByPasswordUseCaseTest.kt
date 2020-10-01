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

class LoginByPasswordUseCaseTest {

    private lateinit var mockRepository: AuthRepository
    private lateinit var useCase: LoginByPasswordUseCase

    @Before
    fun setUp() {
        mockRepository = mock()
        useCase = LoginByPasswordUseCase(
            TestRxSchedulerProviderImpl(),
            mockRepository
        )
    }

    @Test
    fun `When logging in info succeeds, it returns No Error and complete `() {
        val authInfoTest = OAuthResponse(OAuthDataResponse("","", OAuthAttributesResponse("","",0L, "",0L)))

        whenever(
            mockRepository.loginByPasswordWithEmail(any(), any())
        ) doReturn Single.just(authInfoTest).ignoreElement()

        val testSubscriber = useCase
            .execute(
                LoginByPasswordUseCase.Input("email", "12345")
            )
            .test()

        testSubscriber
            .assertNoErrors()
            .assertValueCount(0)
            .assertComplete()
    }

    @Test
    fun `When get authentication info fails, it returns GetLoginError`() {
        whenever(
            mockRepository.loginByPasswordWithEmail(any(), any())
        ) doReturn Single.error<LoginError>(Throwable("Login failed")).ignoreElement()

        val testNegativeSubscriber = useCase
            .execute(
                LoginByPasswordUseCase.Input("email", "12345")
            )
            .test()

        testNegativeSubscriber
            .assertError { it is LoginError }
            .assertValueCount(0)
    }
}
