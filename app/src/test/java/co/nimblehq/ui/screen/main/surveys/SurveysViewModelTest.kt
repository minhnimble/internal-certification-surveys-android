package co.nimblehq.ui.screen.main.surveys

import co.nimblehq.data.error.SurveyError
import co.nimblehq.data.model.Survey
import co.nimblehq.usecase.survey.*
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

@Suppress("IllegalIdentifier")
class SurveysViewModelTest {

    private lateinit var mockDeleteLocalSurveysExcludeIdsCompletableUseCase: DeleteLocalSurveysExcludeIdsCompletableUseCase
    private lateinit var mockGetLocalSurveysSingleUseCase: GetLocalSurveysSingleUseCase
    private lateinit var mockGetSurveysCurrentPageSingleUseCase: GetSurveysCurrentPageSingleUseCase
    private lateinit var mockGetSurveysTotalPagesSingleUseCase: GetSurveysTotalPagesSingleUseCase
    private lateinit var mockLoadSurveysSingleUseCase: LoadSurveysSingleUseCase

    private lateinit var surveysViewModel: SurveysViewModel

    @Before
    fun setUp() {
        mockDeleteLocalSurveysExcludeIdsCompletableUseCase = mock()
        mockGetLocalSurveysSingleUseCase = mock()
        mockGetSurveysCurrentPageSingleUseCase = mock()
        mockGetSurveysTotalPagesSingleUseCase = mock()
        mockLoadSurveysSingleUseCase = mock()
        surveysViewModel = SurveysViewModel(
            mockDeleteLocalSurveysExcludeIdsCompletableUseCase,
            mockGetLocalSurveysSingleUseCase,
            mockGetSurveysCurrentPageSingleUseCase,
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
        whenever(
            mockGetSurveysTotalPagesSingleUseCase.execute(any())
        ) doReturn Single.just(1)
        whenever(
            mockGetSurveysCurrentPageSingleUseCase.execute(any())
        ) doReturn Single.just(1)
        whenever(
            mockLoadSurveysSingleUseCase.execute(any())
        ) doReturn Single.just(listOf())

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
        val sampleSurveysList1 = listOf(Survey("1"), Survey("2"))
        val sampleSurveysList2 = listOf(Survey("3"))
        whenever(
            mockGetLocalSurveysSingleUseCase.execute(any())
        ) doReturn Single.just(sampleSurveysList1)
        whenever(
            mockGetSurveysTotalPagesSingleUseCase.execute(any())
        ) doReturn Single.just(1)
        whenever(
            mockGetSurveysCurrentPageSingleUseCase.execute(any())
        ) doReturn Single.just(1)
        whenever(
            mockLoadSurveysSingleUseCase.execute(any())
        ) doReturn Single.just(sampleSurveysList2)

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
            .assertValueCount(1)
            .assertValues(false)

        surveysPagerItemUiModelsObserver
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueAt(0) {
                it == sampleSurveysList1.map { survey -> survey.toSurveyItemUiModel() }
            }
            .assertValueAt(1) {
                it == sampleSurveysList2.map { survey -> survey.toSurveyItemUiModel() }
            }
    }
}
