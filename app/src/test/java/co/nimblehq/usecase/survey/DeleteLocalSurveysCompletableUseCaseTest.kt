package co.nimblehq.usecase.survey

import co.nimblehq.data.lib.schedulers.TestRxSchedulerProviderImpl
import co.nimblehq.data.storage.dao.SurveyDao
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before
import org.junit.Test

class DeleteLocalSurveysCompletableUseCaseTest {

    private lateinit var surveyDao: SurveyDao
    private lateinit var useCase: DeleteLocalSurveysCompletableUseCase

    @Before
    fun setUp() {
        surveyDao = mock()
        useCase = DeleteLocalSurveysCompletableUseCase(
            TestRxSchedulerProviderImpl(),
            surveyDao
        )
    }

    @Test
    fun `When triggering this use case to clear local surveys data successfully, it always returns Complete`() {
        // Act
        val testSubscriber = useCase.execute(DeleteLocalSurveysCompletableUseCase.Input(listOf())).test()

        // Assert
        testSubscriber
            .assertNoErrors()
            .assertComplete()
    }
}
