package co.nimblehq.usecase.survey

import co.nimblehq.data.error.SurveyError
import co.nimblehq.data.lib.schedulers.TestRxSchedulerProviderImpl
import co.nimblehq.data.repository.SurveyRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import org.junit.Before
import org.junit.Test

class SubmitSurveyResponsesCompletableUseCaseTest {

    private lateinit var mockSurveyRepository: SurveyRepository
    private lateinit var useCase: SubmitSurveyResponsesCompletableUseCase

    @Before
    fun setUp() {
        mockSurveyRepository = mock()
        useCase = SubmitSurveyResponsesCompletableUseCase(
            TestRxSchedulerProviderImpl(),
            mockSurveyRepository
        )
    }

    @Test
    fun `When submitting survey responses succeeds, it returns Complete`() {
        // Arrange
        whenever(
            mockSurveyRepository.submitSurveyResponses(any(), any())
        ) doReturn Completable.complete()

        // Act
        val positiveTestSubscriber = useCase
            .execute(
                SubmitSurveyResponsesCompletableUseCase.Input(
                    "surveyId",
                    listOf()
                )
            )
            .test()

        // Assert
        positiveTestSubscriber
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun `When submitting survey responses fails, it returns SubmitSurveyResponsesError`() {
        // Arrange
        whenever(
            mockSurveyRepository.submitSurveyResponses(any(), any())
        ) doReturn Completable.error(SurveyError.SubmitSurveyResponsesError(null))

        // Act
        val negativeTestSubscriber = useCase
            .execute(
                SubmitSurveyResponsesCompletableUseCase.Input(
                    "surveyId",
                    listOf()
                )
            )
            .test()

        // Assert
        negativeTestSubscriber
            .assertError { it is SurveyError.SubmitSurveyResponsesError }
    }
}
