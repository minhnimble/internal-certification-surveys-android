package co.nimblehq.ui.screen.main.surveys

import co.nimblehq.data.error.SurveyError
import co.nimblehq.data.model.Survey
import co.nimblehq.ui.screen.main.surveys.adapter.toSurveysPagerItemUiModel
import co.nimblehq.usecase.survey.DeleteLocalSurveysCompletableUseCase
import co.nimblehq.usecase.survey.GetInitialSurveysListFlowableUseCase
import co.nimblehq.usecase.survey.LoadMoreSurveysListSingleUseCase
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test

@Suppress("IllegalIdentifier")
class SurveysViewModelTest {

    private lateinit var mockDeleteLocalSurveysCompletableUseCase: DeleteLocalSurveysCompletableUseCase
    private lateinit var mockGetInitialSurveysListFlowableUseCase: GetInitialSurveysListFlowableUseCase
    private lateinit var mockLoadMoreSurveysListSingleUseCase: LoadMoreSurveysListSingleUseCase

    private lateinit var surveysViewModel: SurveysViewModel

    @Before
    fun setUp() {
        mockDeleteLocalSurveysCompletableUseCase = mock()
        mockGetInitialSurveysListFlowableUseCase = mock()
        mockLoadMoreSurveysListSingleUseCase = mock()
        surveysViewModel = SurveysViewModel(
            mockDeleteLocalSurveysCompletableUseCase,
            mockGetInitialSurveysListFlowableUseCase,
            mockLoadMoreSurveysListSingleUseCase
        )
    }

    @Test
    fun `When getting surveys list failed, it triggers a GetSurveysListError`() {
        // Arrange
        whenever(
            mockGetInitialSurveysListFlowableUseCase.execute(any())
        ) doReturn Flowable.error(SurveyError.GetSurveysListError(null))

        // Act
        val errorObserver = surveysViewModel
            .error
            .test()
        surveysViewModel.checkAndLoadInitialSurveysListIfNeeded()

        // Assert
        errorObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { it is SurveyError.GetSurveysListError }
    }

    @Test
    fun `When getting surveys lists successfully, it triggers showLoading as false and assign to surveysPagerItemUiModels for displaying`() {
        // Arrange
        val sampleSurveysList = listOf(Survey())
        whenever(
            mockGetInitialSurveysListFlowableUseCase.execute(any())
        ) doReturn Flowable.just(sampleSurveysList)

        // Act
        val showLoadingObserver = surveysViewModel
            .showLoading
            .test()
        val surveysPagerItemUiModelsObserver = surveysViewModel
            .surveysPagerItemUiModels
            .test()
        surveysViewModel.checkAndLoadInitialSurveysListIfNeeded()

        // Assert
        showLoadingObserver
            .assertNoErrors()
            .assertValueCount(2)
            .assertValues(false, false)

        surveysPagerItemUiModelsObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { it == sampleSurveysList.map { survey -> survey.toSurveysPagerItemUiModel() } }
    }
}
