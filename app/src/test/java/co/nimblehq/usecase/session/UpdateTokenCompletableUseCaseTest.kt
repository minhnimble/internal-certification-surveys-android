package co.nimblehq.usecase.session

import co.nimblehq.data.lib.schedulers.TestRxSchedulerProviderImpl
import co.nimblehq.data.model.AuthData
import co.nimblehq.data.storage.SecureStorage
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before
import org.junit.Test

class UpdateTokenCompletableUseCaseTest {

    private lateinit var secureStorage: SecureStorage
    private lateinit var useCase: UpdateTokenCompletableUseCase

    @Before
    fun setUp() {
        secureStorage = mock()
        useCase = UpdateTokenCompletableUseCase(
            TestRxSchedulerProviderImpl(),
            secureStorage
        )
    }

    @Test
    fun `When trigger the use case to update data, it always returns Complete`() {
        // Act
        val testSubscriber = useCase.execute(
            AuthData(
                "",
                0,
                0,
                "",
                ""
            )
        )
            .test()

        // Assert
        testSubscriber
            .assertNoErrors()
            .assertComplete()
    }
}
