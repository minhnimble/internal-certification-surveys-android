package co.nimblehq.usecase.survey

import co.nimblehq.data.error.SurveyError
import co.nimblehq.data.lib.schedulers.TestRxSchedulerProviderImpl
import co.nimblehq.data.storage.dao.SurveyDao
import com.nhaarman.mockitokotlin2.*
import org.amshove.kluent.itThrows
import org.junit.Before
import org.junit.Test

class DeleteLocalSurveysExcludeIdsCompletableUseCaseTest {

    private lateinit var surveyDao: SurveyDao
    private lateinit var useCase: DeleteLocalSurveysExcludeIdsCompletableUseCase

    @Before
    fun setUp() {
        surveyDao = mock()
        useCase = DeleteLocalSurveysExcludeIdsCompletableUseCase(
            TestRxSchedulerProviderImpl(),
            surveyDao
        )
    }

    @Test
    fun `When triggering this use case to clear local surveys data successfully, it returns Complete`() {
        // Act
        val testSubscriber = useCase.execute(DeleteLocalSurveysExcludeIdsCompletableUseCase.Input(listOf())).test()

        // Assert
        testSubscriber
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun `When triggering this use case to clear local surveys data failed, it returns DeleteLocalSurveysError`() {
        // Arrange
        whenever(
            surveyDao.deleteSurveys(any())
        ).then { throw SurveyError.DeleteLocalSurveysError(null) }

        // Act
        val negativeTestSubscriber = useCase.execute(DeleteLocalSurveysExcludeIdsCompletableUseCase.Input(listOf())).test()

        // Assert
        negativeTestSubscriber
            .assertValueCount(0)
            .assertError { it is SurveyError.DeleteLocalSurveysError }
    }
}
