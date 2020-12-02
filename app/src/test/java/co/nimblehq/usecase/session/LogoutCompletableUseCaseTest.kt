package co.nimblehq.usecase.session

import co.nimblehq.data.error.SessionError
import co.nimblehq.data.lib.schedulers.TestRxSchedulerProviderImpl
import co.nimblehq.data.repository.AuthRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import org.junit.Before
import org.junit.Test

class LogoutCompletableUseCaseTest {

    private lateinit var mockRepository: AuthRepository
    private lateinit var useCase: LogoutCompletableUseCase

    @Before
    fun setUp() {
        mockRepository = mock()
        useCase = LogoutCompletableUseCase(
            TestRxSchedulerProviderImpl(),
            mockRepository
        )
    }

    @Test
    fun `When logging out successfully, it returns Complete`() {
        // Arrange
        whenever(
            mockRepository.logout(any())
        ) doReturn Completable.complete()

        // Act
        val positiveTestSubscriber = useCase
            .execute("token")
            .test()

        // Assert
        positiveTestSubscriber
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun `When logging out failed, it returns a LogoutError`() {
        // Arrange
        whenever(
            mockRepository.logout(any())
        ) doReturn  Completable.error(SessionError.LogoutError())

        // Act
        val negativeTestSubscriber = useCase
            .execute("token")
            .test()

        // Assert
        negativeTestSubscriber
            .assertError { it is SessionError.LogoutError }
    }
}
