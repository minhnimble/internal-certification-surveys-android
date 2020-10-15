package co.nimblehq.ui.screen.main.surveys

import co.nimblehq.data.error.SurveyError
import co.nimblehq.data.model.Survey
import co.nimblehq.ui.screen.main.surveys.adapter.toSurveysPagerItemUiModel
import co.nimblehq.usecase.survey.GetLocalSurveysListSingleUseCase
import co.nimblehq.usecase.survey.LoadSurveysListSingleUseCase
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

@Suppress("IllegalIdentifier")
class SurveysViewModelTest {

    private lateinit var mockGetLocalSurveysListSingleUseCase: GetLocalSurveysListSingleUseCase
    private lateinit var mockLoadSurveysListSingleUseCase: LoadSurveysListSingleUseCase

    private lateinit var surveysViewModel: SurveysViewModel

    @Before
    fun setUp() {
        mockGetLocalSurveysListSingleUseCase = mock()
        mockLoadSurveysListSingleUseCase = mock()
        surveysViewModel = SurveysViewModel(
            mockGetLocalSurveysListSingleUseCase,
            mockLoadSurveysListSingleUseCase
        )
    }

    @Test
    fun `When getting local surveys failed, it triggers a GetSurveysListError`() {
        // Arrange
        whenever(
            mockGetLocalSurveysListSingleUseCase.execute(any())
        ) doReturn Single.error(SurveyError.GetSurveysListError(null))

        // Act
        val errorObserver = surveysViewModel
            .error
            .test()
        surveysViewModel.checkAndLoadSurveysListIfNeeded()

        // Assert
        errorObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { it is SurveyError.GetSurveysListError }
    }

    @Test
    fun `When getting local surveys successfully and there is at least one survey items, it triggers showLoading as false and assign to surveysPagerItemUiModels for displaying`() {
        // Arrange
        val sampleSurveysList = listOf(Survey())
        whenever(
            mockGetLocalSurveysListSingleUseCase.execute(any())
        ) doReturn Single.just(sampleSurveysList)

        // Act
        val showLoadingObserver = surveysViewModel
            .showLoading
            .test()
        val surveysPagerItemUiModelsObserver = surveysViewModel
            .surveysPagerItemUiModels
            .test()
        surveysViewModel.checkAndLoadSurveysListIfNeeded()

        // Assert
        showLoadingObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { !it }

        surveysPagerItemUiModelsObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { it.size == sampleSurveysList.size }
            .assertValue { it == sampleSurveysList.map { survey -> survey.toSurveysPagerItemUiModel() } }
    }

    @Test
    fun `When loading surveys list from backend successes, it triggers showLoading as false and assign to surveysPagerItemUiModels for displaying`() {
        // Arrange
        val sampleSurveysList = listOf(Survey())
        whenever(
            mockGetLocalSurveysListSingleUseCase.execute(any())
        ) doReturn Single.just(listOf())
        whenever(
            mockLoadSurveysListSingleUseCase.execute(any())
        ) doReturn Single.just(sampleSurveysList)

        // Act
        val showLoadingObserver = surveysViewModel
            .showLoading
            .test()
        val surveysPagerItemUiModelsObserver = surveysViewModel
            .surveysPagerItemUiModels
            .test()
        surveysViewModel.checkAndLoadSurveysListIfNeeded()

        // Assert
        showLoadingObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { !it }

        surveysPagerItemUiModelsObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { it.size == sampleSurveysList.size }
            .assertValue { it == sampleSurveysList.map { survey -> survey.toSurveysPagerItemUiModel() } }
    }

    @Test
    fun `When loading surveys list from backend fails, it triggers showLoading as false and returns a GetSurveysListError`() {
        // Arrange
        whenever(
            mockGetLocalSurveysListSingleUseCase.execute(any())
        ) doReturn Single.just(listOf())
        whenever(
            mockLoadSurveysListSingleUseCase.execute(any())
        ) doReturn Single.error(SurveyError.GetSurveysListError(null))

        // Act
        val errorObserver = surveysViewModel
            .error
            .test()
        val showLoadingObserver = surveysViewModel
            .showLoading
            .test()

        surveysViewModel.checkAndLoadSurveysListIfNeeded()

        // Assert
        showLoadingObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { !it }

        errorObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { it is SurveyError.GetSurveysListError }
    }
}
