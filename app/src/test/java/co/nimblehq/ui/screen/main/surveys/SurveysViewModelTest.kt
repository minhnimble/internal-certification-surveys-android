package co.nimblehq.ui.screen.main.surveys

import co.nimblehq.data.error.SurveyError
import co.nimblehq.data.model.Survey
import co.nimblehq.usecase.survey.DeleteLocalSurveysCompletableUseCase
import co.nimblehq.usecase.survey.GetLocalSurveysSingleUseCase
import co.nimblehq.usecase.survey.GetSurveysTotalPagesSingleUseCase
import co.nimblehq.usecase.survey.LoadSurveysSingleUseCase
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

@Suppress("IllegalIdentifier")
class SurveysViewModelTest {

    private lateinit var mockDeleteLocalSurveysCompletableUseCase: DeleteLocalSurveysCompletableUseCase
    private lateinit var mockGetLocalSurveysSingleUseCase: GetLocalSurveysSingleUseCase
    private lateinit var mockGetSurveysTotalPagesSingleUseCase: GetSurveysTotalPagesSingleUseCase
    private lateinit var mockLoadSurveysSingleUseCase: LoadSurveysSingleUseCase

    private lateinit var surveysViewModel: SurveysViewModel

    @Before
    fun setUp() {
        mockDeleteLocalSurveysCompletableUseCase = mock()
        mockGetLocalSurveysSingleUseCase = mock()
        mockGetSurveysTotalPagesSingleUseCase = mock()
        mockLoadSurveysSingleUseCase = mock()
        surveysViewModel = SurveysViewModel(
            mockDeleteLocalSurveysCompletableUseCase,
            mockGetLocalSurveysSingleUseCase,
            mockGetSurveysTotalPagesSingleUseCase,
            mockLoadSurveysSingleUseCase
        )
    }

    @Test
    fun `When getting initial surveys list failed, it triggers a GetSurveysListError`() {
        // Arrange
        whenever(
            mockGetLocalSurveysSingleUseCase.execute(any())
        ) doReturn Single.error(SurveyError.GetSurveysError(null))

        // Act
        val errorObserver = surveysViewModel
            .error
            .test()
        surveysViewModel.checkAndRefreshInitialSurveys()

        // Assert
        errorObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { it is SurveyError.GetSurveysError }
    }

    @Test
    fun `When getting initial surveys list successfully, it triggers showLoading as false and assign to surveysPagerItemUiModels for displaying`() {
        // Arrange
        val sampleSurveysList = listOf(Survey())
        whenever(
            mockGetLocalSurveysSingleUseCase.execute(any())
        ) doReturn Single.just(sampleSurveysList)
        whenever(
            mockGetSurveysTotalPagesSingleUseCase.execute(any())
        ) doReturn Single.just(1)

        // Act
        val showLoadingObserver = surveysViewModel
            .showLoading
            .test()
        val surveysPagerItemUiModelsObserver = surveysViewModel
            .surveyItemUiModels
            .test()
        surveysViewModel.checkAndRefreshInitialSurveys()

        // Assert
        showLoadingObserver
            .assertNoErrors()
            .assertValueCount(2)
            .assertValues(false, false)

        surveysPagerItemUiModelsObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { it == sampleSurveysList.map { survey -> survey.toSurveyItemUiModel() } }
    }
}
