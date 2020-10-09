package co.nimblehq.usecase.session

import co.nimblehq.data.lib.schedulers.TestRxSchedulerProviderImpl
import co.nimblehq.data.storage.SecureStorage
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before
import org.junit.Test

class ClearTokenCompletableUseCaseTest {

    private lateinit var secureStorage: SecureStorage
    private lateinit var useCase: ClearTokenCompletableUseCase

    @Before
    fun setUp() {
        secureStorage = mock()
        useCase = ClearTokenCompletableUseCase(
            TestRxSchedulerProviderImpl(),
            secureStorage
        )
    }

    @Test
    fun `When triggering this use case to clear data, it always returns Complete`() {
        // Act
        val testSubscriber = useCase.execute(Unit).test()

        // Assert
        testSubscriber
            .assertNoErrors()
            .assertComplete()
    }
}
