package co.nimblehq.ui.screen.main.surveys

import co.nimblehq.data.error.SurveyError
import co.nimblehq.data.lib.common.DEFAULT_UNSELECTED_INDEX
import co.nimblehq.data.model.Survey
import co.nimblehq.usecase.survey.*
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
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
    fun `When getting initial surveys failed, it triggers a GetSurveysError`() {
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
    fun `When getting initial surveys successfully, it triggers showLoading as false and assign to surveysPagerItemUiModels for displaying`() {
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

    @Test
    fun `When refreshing surveys successfully, it triggers showLoading as false, resets selected survey & index to first item and assign to surveysPagerItemUiModels for displaying`() {
        // Arrange
        val sampleSurveysList = listOf(Survey(id = "1"), Survey(id = "2"))
        whenever(
            mockDeleteLocalSurveysCompletableUseCase.execute(any())
        ) doReturn Completable.complete()
        whenever(
            mockLoadMoreSurveysSingleUseCase.execute(any())
        ) doReturn Single.just(sampleSurveysList)

        // Act
        val showLoadingObserver = surveysViewModel
            .showLoading
            .test()
        val surveysPagerItemUiModelsObserver = surveysViewModel
            .surveyItemUiModels
            .test()
        val selectedSurveyDataObserver = surveysViewModel
            .selectedSurveyData
            .test()
        surveysViewModel.refreshSurveysList()

        // Assert
        showLoadingObserver
            .assertNoErrors()
            .assertValueCount(2)
            .assertValues(true, false)

        selectedSurveyDataObserver
            .assertNoErrors()
            .assertValueCount(3)
            .assertValueAt(0) {
                it.first == DEFAULT_UNSELECTED_INDEX && it.second?.id == null // Initial
            }
            .assertValueAt(1) {
                it.first == DEFAULT_UNSELECTED_INDEX && it.second?.id == null // Reset
            }
            .assertValueAt(2) {
                it.first == 0 && it.second?.id == "1" // Assign
            }

        surveysPagerItemUiModelsObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { it == sampleSurveysList.map { survey -> survey.toSurveyItemUiModel() } }
    }

    @Test
    fun `When refreshing surveys failed, it triggers showLoading as false, and returns GetSurveysError`() {
        // Arrange
        whenever(
            mockDeleteLocalSurveysCompletableUseCase.execute(any())
        ) doReturn Completable.complete()
        whenever(
            mockLoadMoreSurveysSingleUseCase.execute(any())
        ) doReturn Single.error(SurveyError.GetSurveysError(null))

        // Act
        val showLoadingObserver = surveysViewModel
            .showLoading
            .test()
        val errorObserver = surveysViewModel
            .error
            .test()
        surveysViewModel.refreshSurveysList()

        // Assert
        showLoadingObserver
            .assertNoErrors()
            .assertValueCount(2)
            .assertValues(true, false)

        errorObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { it is SurveyError.GetSurveysError }
    }

    @Test
    fun `When user swipe to previous page and the new value is valid, it updates selected survey & index to previous item for displaying`() {
        // Arrange
        val sampleSurveysList = listOf(Survey(id = "1"), Survey(id = "2"))
        whenever(
            mockDeleteLocalSurveysCompletableUseCase.execute(any())
        ) doReturn Completable.complete()
        whenever(
            mockLoadMoreSurveysSingleUseCase.execute(any())
        ) doReturn Single.just(sampleSurveysList)
        surveysViewModel.refreshSurveysList() // Fill the sample list
        surveysViewModel.nextIndex()

        // Act
        val selectedSurveyDataObserver = surveysViewModel
            .selectedSurveyData
            .test()
        surveysViewModel.previousIndex()

        // Assert
        selectedSurveyDataObserver
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueAt(1) {
                it.first == 0 && it.second?.id == "1"
            }
    }

    @Test
    fun `When user swipe to previous page and the new value is invalid, it does not update anything`() {
        // Arrange
        val sampleSurveysList = listOf(Survey(id = "1"), Survey(id = "2"))
        whenever(
            mockDeleteLocalSurveysCompletableUseCase.execute(any())
        ) doReturn Completable.complete()
        whenever(
            mockLoadMoreSurveysSingleUseCase.execute(any())
        ) doReturn Single.just(sampleSurveysList)
        surveysViewModel.refreshSurveysList() // Fill the sample list

        // Act
        val selectedSurveyDataObserver = surveysViewModel
            .selectedSurveyData
            .test()
        surveysViewModel.previousIndex()

        // Assert
        selectedSurveyDataObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue {
                it.first == 0 && it.second?.id == "1"
            }
    }

    @Test
    fun `When user swipe to next page and the new value is valid, it updates selected survey & index to next item for displaying`() {
        // Arrange
        val sampleSurveysList = listOf(Survey(id = "1"), Survey(id = "2"))
        whenever(
            mockDeleteLocalSurveysCompletableUseCase.execute(any())
        ) doReturn Completable.complete()
        whenever(
            mockLoadMoreSurveysSingleUseCase.execute(any())
        ) doReturn Single.just(sampleSurveysList)
        surveysViewModel.refreshSurveysList() // Fill the sample list

        // Act
        val selectedSurveyDataObserver = surveysViewModel
            .selectedSurveyData
            .test()
        surveysViewModel.nextIndex()

        // Assert
        selectedSurveyDataObserver
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueAt(1) {
                it.first == 1 && it.second?.id == "2"
            }
    }

    @Test
    fun `When user swipe to next page and the new value is invalid, it doesn't update anything`() {
        // Arrange
        val sampleSurveysList = listOf(Survey(id = "1"), Survey(id = "2"))
        whenever(
            mockDeleteLocalSurveysCompletableUseCase.execute(any())
        ) doReturn Completable.complete()
        whenever(
            mockLoadMoreSurveysSingleUseCase.execute(any())
        ) doReturn Single.just(sampleSurveysList)
        surveysViewModel.refreshSurveysList() // Fill the sample list
        surveysViewModel.nextIndex() // Send the current index to last item

        // Act
        val selectedSurveyDataObserver = surveysViewModel
            .selectedSurveyData
            .test()
        surveysViewModel.nextIndex()

        // Assert
        selectedSurveyDataObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue {
                it.first == 1 && it.second?.id == "2"
            }
    }
}
