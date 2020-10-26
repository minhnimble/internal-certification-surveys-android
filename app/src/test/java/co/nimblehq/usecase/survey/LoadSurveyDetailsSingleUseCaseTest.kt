package co.nimblehq.usecase.survey

import co.nimblehq.data.error.SurveyError
import co.nimblehq.data.lib.schedulers.TestRxSchedulerProviderImpl
import co.nimblehq.data.repository.SurveyRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class LoadSurveyDetailsSingleUseCaseTest {

    private lateinit var surveyRepository: SurveyRepository
    private lateinit var useCase: LoadSurveyDetailsSingleUseCase

    @Before
    fun setUp() {
        surveyRepository = mock()
        useCase = LoadSurveyDetailsSingleUseCase(
            TestRxSchedulerProviderImpl(),
            surveyRepository
        )
    }

    @Test
    fun `When loading survey details from backend successfully, it returns a survey`() {
        // Arrange
        whenever(
            surveyRepository.loadSurveyDetails(any())
        ) doReturn Single.just(listOf())

        // Act
        val positiveTestSubscriber = useCase
            .execute("Survey Id")
            .test()

        // Assert
        positiveTestSubscriber
            .assertNoErrors()
            .assertValueCount(1)
    }

    @Test
    fun `When loading survey details from backend failed, it returns GetSurveyDetailsError`() {
        // Arrange
        whenever(
            surveyRepository.loadSurveyDetails(any())
        ) doReturn Single.error(SurveyError.GetSurveyDetailsError(null))

        // Act
        val negativeTestSubscriber = useCase
            .execute("Survey Id")
            .test()

        // Assert
        negativeTestSubscriber
            .assertValueCount(0)
            .assertError { it is SurveyError.GetSurveyDetailsError }
    }
}
