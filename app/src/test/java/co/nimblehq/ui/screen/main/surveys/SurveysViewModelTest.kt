package co.nimblehq.ui.screen.main.surveys

import co.nimblehq.data.error.SurveyError
import co.nimblehq.data.lib.common.DEFAULT_UNSELECTED_INDEX
import co.nimblehq.data.model.Survey
import co.nimblehq.usecase.session.ClearLocalTokenCompletableUseCase
import co.nimblehq.usecase.session.GetLocalUserTokenSingleUseCase
import co.nimblehq.usecase.session.LogoutCompletableUseCase
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

    private lateinit var mockClearLocalTokenCompletableUseCase: ClearLocalTokenCompletableUseCase
    private lateinit var mockDeleteLocalSurveysExcludeIdsCompletableUseCase: DeleteLocalSurveysExcludeIdsCompletableUseCase
    private lateinit var mockGetLocalSurveysSingleUseCase: GetLocalSurveysSingleUseCase
    private lateinit var mockGetLocalTokenCompletableUseCase: GetLocalUserTokenSingleUseCase
    private lateinit var mockGetSurveysCurrentPageSingleUseCase: GetSurveysCurrentPageSingleUseCase
    private lateinit var mockGetSurveysTotalPagesSingleUseCase: GetSurveysTotalPagesSingleUseCase
    private lateinit var mockLoadSurveysSingleUseCase: LoadSurveysSingleUseCase
    private lateinit var mockLogoutCompletableUseCase: LogoutCompletableUseCase

    private lateinit var surveysViewModel: SurveysViewModel

    @Before
    fun setUp() {
        mockClearLocalTokenCompletableUseCase = mock()
        mockDeleteLocalSurveysExcludeIdsCompletableUseCase = mock()
        mockGetLocalSurveysSingleUseCase = mock()
        mockGetLocalTokenCompletableUseCase = mock()
        mockGetSurveysCurrentPageSingleUseCase = mock()
        mockGetSurveysTotalPagesSingleUseCase = mock()
        mockLoadSurveysSingleUseCase = mock()
        mockLogoutCompletableUseCase = mock()
        surveysViewModel = SurveysViewModel(
            mockClearLocalTokenCompletableUseCase,
            mockDeleteLocalSurveysExcludeIdsCompletableUseCase,
            mockGetLocalSurveysSingleUseCase,
            mockGetLocalTokenCompletableUseCase,
            mockGetSurveysCurrentPageSingleUseCase,
            mockGetSurveysTotalPagesSingleUseCase,
            mockLoadSurveysSingleUseCase,
            mockLogoutCompletableUseCase
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
            mockDeleteLocalSurveysExcludeIdsCompletableUseCase.execute(any())
        ) doReturn Completable.complete()
        whenever(
            mockLoadSurveysSingleUseCase.execute(any())
        ) doReturn Single.just(sampleSurveysList)
        whenever(
            mockGetSurveysTotalPagesSingleUseCase.execute(any())
        ) doReturn Single.just(1)

        // Act
        val showRefreshingObserver = surveysViewModel
            .showRefreshing
            .test()
        val surveysPagerItemUiModelsObserver = surveysViewModel
            .surveyItemUiModels
            .test()
        val selectedSurveyIndexObserver = surveysViewModel
            .selectedSurveyIndex
            .test()
        surveysViewModel.refreshSurveysList()

        // Assert
        showRefreshingObserver
            .assertNoErrors()
            .assertValueCount(2)
            .assertValues(true, false)

        selectedSurveyIndexObserver
            .assertNoErrors()
            .assertValueCount(2)
            .assertValues(DEFAULT_UNSELECTED_INDEX, 0) // Initial, reset, refresh

        surveysPagerItemUiModelsObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { it == sampleSurveysList.map { survey -> survey.toSurveyItemUiModel() } }
    }

    @Test
    fun `When refreshing surveys failed, it triggers showLoading as false, and returns GetSurveysError`() {
        // Arrange
        whenever(
            mockDeleteLocalSurveysExcludeIdsCompletableUseCase.execute(any())
        ) doReturn Completable.complete()
        whenever(
            mockGetSurveysTotalPagesSingleUseCase.execute(any())
        ) doReturn Single.just(1)
        whenever(
            mockLoadSurveysSingleUseCase.execute(any())
        ) doReturn Single.error(SurveyError.GetSurveysError(null))

        // Act
        val showRefreshingObserver = surveysViewModel
            .showRefreshing
            .test()
        val errorObserver = surveysViewModel
            .error
            .test()
        surveysViewModel.refreshSurveysList()

        // Assert
        showRefreshingObserver
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
            mockDeleteLocalSurveysExcludeIdsCompletableUseCase.execute(any())
        ) doReturn Completable.complete()
        whenever(
            mockLoadSurveysSingleUseCase.execute(any())
        ) doReturn Single.just(sampleSurveysList)
        whenever(
            mockGetSurveysTotalPagesSingleUseCase.execute(any())
        ) doReturn Single.just(1)
        surveysViewModel.refreshSurveysList() // Fill the sample list
        surveysViewModel.nextIndex()

        // Act
        val selectedSurveyIndexObserver = surveysViewModel
            .selectedSurveyIndex
            .test()
        surveysViewModel.previousIndex()

        // Assert
        selectedSurveyIndexObserver
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueAt(1) {
                it == 0
            }
    }

    @Test
    fun `When user swipe to previous page and the new value is invalid, it does not update anything`() {
        // Arrange
        val sampleSurveysList = listOf(Survey(id = "1"), Survey(id = "2"))
        whenever(
            mockDeleteLocalSurveysExcludeIdsCompletableUseCase.execute(any())
        ) doReturn Completable.complete()
        whenever(
            mockLoadSurveysSingleUseCase.execute(any())
        ) doReturn Single.just(sampleSurveysList)
        whenever(
            mockGetSurveysTotalPagesSingleUseCase.execute(any())
        ) doReturn Single.just(1)
        surveysViewModel.refreshSurveysList() // Fill the sample list

        // Act
        val selectedSurveyIndexObserver = surveysViewModel
            .selectedSurveyIndex
            .test()
        surveysViewModel.previousIndex()

        // Assert
        selectedSurveyIndexObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { it == 0 }
    }

    @Test
    fun `When user swipe to next page and the new value is valid, it updates selected survey & index to next item for displaying`() {
        // Arrange
        val sampleSurveysList = listOf(Survey(id = "1"), Survey(id = "2"))
        whenever(
            mockDeleteLocalSurveysExcludeIdsCompletableUseCase.execute(any())
        ) doReturn Completable.complete()
        whenever(
            mockLoadSurveysSingleUseCase.execute(any())
        ) doReturn Single.just(sampleSurveysList)
        whenever(
            mockGetSurveysTotalPagesSingleUseCase.execute(any())
        ) doReturn Single.just(1)
        surveysViewModel.refreshSurveysList() // Fill the sample list

        // Act
        val selectedSurveyIndexObserver = surveysViewModel
            .selectedSurveyIndex
            .test()
        surveysViewModel.nextIndex()

        // Assert
        selectedSurveyIndexObserver
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueAt(1) { it == 1 }
    }

    @Test
    fun `When user swipe to next page and the new value is invalid and there is no more item to load, it doesn't update anything and fires NoMoreSurveyError`() {
        // Arrange
        val sampleSurveysList = listOf(Survey(id = "1"), Survey(id = "2"))
        whenever(
            mockDeleteLocalSurveysExcludeIdsCompletableUseCase.execute(any())
        ) doReturn Completable.complete()
        whenever(
            mockLoadSurveysSingleUseCase.execute(any())
        ) doReturn Single.just(sampleSurveysList)
        whenever(
            mockGetSurveysCurrentPageSingleUseCase.execute(any())
        ) doReturn Single.just(1)
        whenever(
            mockGetSurveysTotalPagesSingleUseCase.execute(any())
        ) doReturn Single.just(1)
        surveysViewModel.refreshSurveysList() // Fill the sample list
        surveysViewModel.nextIndex() // Send the current index to last item

        // Act
        val selectedSurveyIndexObserver = surveysViewModel
            .selectedSurveyIndex
            .test()
        surveysViewModel.nextIndex()

        val errorObserver = surveysViewModel
            .error
            .test()
        surveysViewModel.nextIndex()

        // Assert
        selectedSurveyIndexObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { it == 1 }

        errorObserver
            .assertValueCount(1)
            .assertValue { it is SurveyError.NoMoreSurveysError }
    }
}
