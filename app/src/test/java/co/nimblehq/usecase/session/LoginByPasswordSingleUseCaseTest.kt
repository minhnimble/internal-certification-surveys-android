package co.nimblehq.usecase.session

import co.nimblehq.data.error.LoginError
import co.nimblehq.data.lib.schedulers.TestRxSchedulerProviderImpl
import co.nimblehq.data.repository.AuthRepository
import co.nimblehq.data.model.AuthData
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
        ) doReturn Single.just(
            AuthData()
        )

        // Act
        val positiveTestSubscriber = useCase
            .execute(
                LoginByPasswordSingleUseCase.Input("email", "12345")
            )
            .test()

        // Assert
        positiveTestSubscriber
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
        val negativeTestSubscriber = useCase
            .execute(
                LoginByPasswordSingleUseCase.Input("email", "12345")
            )
            .test()

        // Assert
        negativeTestSubscriber
            .assertError { it is LoginError }
    }
}
