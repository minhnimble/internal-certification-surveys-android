package co.nimblehq.usecase.session

import co.nimblehq.data.lib.schedulers.TestRxSchedulerProviderImpl
import co.nimblehq.data.model.AuthData
import co.nimblehq.data.storage.SecureStorage
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before
import org.junit.Test

class UpdateLocalUserTokenCompletableUseCaseTest {

    private lateinit var secureStorage: SecureStorage
    private lateinit var useCase: UpdateLocalUserTokenCompletableUseCase

    @Before
    fun setUp() {
        secureStorage = mock()
        useCase = UpdateLocalUserTokenCompletableUseCase(
            TestRxSchedulerProviderImpl(),
            secureStorage
        )
    }

    @Test
    fun `When triggering this use case to update secureStorage with a new AuthData object, it always returns Complete`() {
        // Act
        val positiveTestSubscriber = useCase.execute(AuthData()).test()

        // Assert
        positiveTestSubscriber
            .assertNoErrors()
            .assertComplete()
    }
}
