package co.nimblehq.usecase.survey

import co.nimblehq.data.error.SurveyError
import co.nimblehq.data.lib.schedulers.TestRxSchedulerProviderImpl
import co.nimblehq.data.storage.dao.SurveyDao
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class GetLocalSurveysSingleUseCaseTest {

    private lateinit var surveyDao: SurveyDao
    private lateinit var useCase: GetLocalSurveysSingleUseCase

    @Before
    fun setUp() {
        surveyDao = mock()
        useCase = GetLocalSurveysSingleUseCase(
            TestRxSchedulerProviderImpl(),
            surveyDao
        )
    }

    @Test
    fun `When getting surveys list successfully, it returns a list of surveys`() {
        // Arrange
        whenever(
            surveyDao.getAllSurveys()
        ) doReturn Single.just(listOf())

        // Act
        val positiveTestSubscriber = useCase
            .execute(Unit)
            .test()

        // Assert
        positiveTestSubscriber
            .assertNoErrors()
            .assertValueCount(1)
    }

    @Test
    fun `When getting surveys list failed, it returns GetSurveysListError`() {
        // Arrange
        whenever(
            surveyDao.getAllSurveys()
        ) doReturn Single.error(Throwable(null, null))

        // Act
        val negativeTestSubscriber = useCase
            .execute(Unit)
            .test()

        // Assert
        negativeTestSubscriber
            .assertValueCount(0)
            .assertError { it is SurveyError.GetSurveysError }
    }
}
