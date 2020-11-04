package co.nimblehq.ui.screen.main.surveydetails

import co.nimblehq.data.error.SurveyError
import co.nimblehq.data.model.Question
import co.nimblehq.ui.screen.main.surveydetails.uimodel.toQuestionItemPagerUiModels
import co.nimblehq.usecase.survey.LoadSurveyDetailsSingleUseCase
import co.nimblehq.usecase.survey.SubmitSurveyResponsesCompletableUseCase
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

@Suppress("IllegalIdentifier")
class SurveyDetailsViewModelTest {

    private lateinit var mockLoadSurveyDetailsSingleUseCase: LoadSurveyDetailsSingleUseCase
    private lateinit var mockSubmitSurveyResponsesCompletableUseCase: SubmitSurveyResponsesCompletableUseCase

    private lateinit var surveyDetailsViewModel: SurveyDetailsViewModel

    @Before
    fun setUp() {
        mockLoadSurveyDetailsSingleUseCase = mock()
        mockSubmitSurveyResponsesCompletableUseCase = mock()
        surveyDetailsViewModel = SurveyDetailsViewModel(
            mockLoadSurveyDetailsSingleUseCase,
            mockSubmitSurveyResponsesCompletableUseCase
        )
    }

    @Test
    fun `When loading survey details failed, it triggers a GetSurveyDetailsError`() {
        // Arrange
        val surveyId = "Survey Id"
        whenever(
            mockLoadSurveyDetailsSingleUseCase.execute(any())
        ) doReturn Single.error(SurveyError.GetSurveyDetailsError(null))

        // Act
        val errorObserver = surveyDetailsViewModel
            .showError
            .test()
        surveyDetailsViewModel.loadSurveyDetails(surveyId)

        // Assert
        errorObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { it is SurveyError.GetSurveyDetailsError }
    }

    @Test
    fun `When loading survey details successfully, it triggers showLoading as false and assign to questionItemPagerUiModels for displaying`() {
        // Arrange
        val questions = listOf(Question("1"), Question("2"))
        whenever(
            mockLoadSurveyDetailsSingleUseCase.execute(any())
        ) doReturn Single.just(questions)

        // Act
        val showLoadingObserver = surveyDetailsViewModel
            .showLoading
            .test()
        val questionItemPagerUiModelsObserver = surveyDetailsViewModel
            .questionItemPagerUiModels
            .test()
        surveyDetailsViewModel.loadSurveyDetails("Survey Id")

        // Assert
        showLoadingObserver
            .assertNoErrors()
            .assertValueCount(2)
            .assertValues(true, false)

        questionItemPagerUiModelsObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue {
                it == questions.toQuestionItemPagerUiModels()
            }
    }
}
