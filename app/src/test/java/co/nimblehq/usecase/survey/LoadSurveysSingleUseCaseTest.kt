package co.nimblehq.usecase.survey

import co.nimblehq.data.error.SurveyError
import co.nimblehq.data.lib.schedulers.TestRxSchedulerProviderImpl
import co.nimblehq.data.model.Survey
import co.nimblehq.data.repository.SurveyRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class LoadSurveysSingleUseCaseTest {

    private lateinit var surveyRepository: SurveyRepository
    private lateinit var useCase: LoadSurveysSingleUseCase

    @Before
    fun setUp() {
        surveyRepository = mock()
        useCase = LoadSurveysSingleUseCase(
            TestRxSchedulerProviderImpl(),
            surveyRepository
        )
    }

    @Test
    fun `When loading surveys list from backend successfully, it returns a list of surveys`() {
        // Arrange
        whenever(
            surveyRepository.loadSurveys(any(), any())
        ) doReturn Single.just(listOf(Survey()))

        // Act
        val positiveTestSubscriber = useCase
            .execute(
                LoadSurveysSingleUseCase.Input(
                    1,
                    10
                )
            )
            .test()

        // Assert
        positiveTestSubscriber
            .assertNoErrors()
            .assertValueCount(1)
    }

    @Test
    fun `When loading surveys list from backend failed, it returns GetSurveysListError`() {
        // Arrange
        whenever(
            surveyRepository.loadSurveys(any(), any())
        ) doReturn Single.error(SurveyError.GetSurveysError(null))

        // Act
        val negativeTestSubscriber = useCase
            .execute(
                LoadSurveysSingleUseCase.Input(
                    1,
                    10
                )
            )
            .test()

        // Assert
        negativeTestSubscriber
            .assertValueCount(0)
            .assertError { it is SurveyError.GetSurveysError }
    }
}
