package co.nimblehq.ui.screen.main.surveys

import co.nimblehq.data.error.SessionError
import co.nimblehq.data.error.SurveyError
import co.nimblehq.data.lib.common.DEFAULT_UNSELECTED_INDEX
import co.nimblehq.data.model.Survey
import co.nimblehq.event.NavigationEvent
import co.nimblehq.usecase.session.FullLogoutCompletableUseCase
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
    private lateinit var mockFullLogoutCompletableUseCase: FullLogoutCompletableUseCase
    private lateinit var mockGetLocalSurveysSingleUseCase: GetLocalSurveysSingleUseCase
    private lateinit var mockGetSurveysCurrentPageSingleUseCase: GetSurveysCurrentPageSingleUseCase
    private lateinit var mockGetSurveysTotalPagesSingleUseCase: GetSurveysTotalPagesSingleUseCase
    private lateinit var mockLoadSurveysSingleUseCase: LoadSurveysSingleUseCase

    private lateinit var surveysViewModel: SurveysViewModel

    @Before
    fun setUp() {
        mockDeleteLocalSurveysExcludeIdsCompletableUseCase = mock()
        mockFullLogoutCompletableUseCase = mock()
        mockGetLocalSurveysSingleUseCase = mock()
        mockGetSurveysCurrentPageSingleUseCase = mock()
        mockGetSurveysTotalPagesSingleUseCase = mock()
        mockLoadSurveysSingleUseCase = mock()
        surveysViewModel = SurveysViewModel(
            mockDeleteLocalSurveysExcludeIdsCompletableUseCase,
            mockFullLogoutCompletableUseCase,
            mockGetLocalSurveysSingleUseCase,
            mockGetSurveysCurrentPageSingleUseCase,
            mockGetSurveysTotalPagesSingleUseCase,
            mockLoadSurveysSingleUseCase
        )
    }

    @Test
    fun `When getting local cached surveys failed, it triggers a GetSurveysError`() {
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
        surveysViewModel.getLocalCachedSurveys()

        // Assert
        errorObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { it is SurveyError.GetSurveysError }
    }

    @Test
    fun `When getting local cached surveys successfully, it triggers showLoading as false and assign to surveysPagerItemUiModels for displaying`() {
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
        whenever(
            mockDeleteLocalSurveysExcludeIdsCompletableUseCase.execute(any())
        ) doReturn Completable.complete()

        // Act
        val showLoadingObserver = surveysViewModel
            .showLoading
            .test()
        val surveysPagerItemUiModelsObserver = surveysViewModel
            .surveyItemUiModels
            .test()
        surveysViewModel.getLocalCachedSurveys()

        // Assert
        showLoadingObserver
            .assertNoErrors()
            .assertValueCount(3)
            .assertValues(true, false, false)

        surveysPagerItemUiModelsObserver
            .assertNoErrors()
            .assertValueCount(2)
            .assertValues(
                sampleSurveysList1.map { survey -> survey.toSurveyItemUiModel() },
                sampleSurveysList2.map { survey -> survey.toSurveyItemUiModel() }
            )
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
        surveysViewModel.refreshSurveys()

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
        surveysViewModel.refreshSurveys()

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
        surveysViewModel.refreshSurveys() // Fill the sample list
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
        surveysViewModel.refreshSurveys() // Fill the sample list

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
        surveysViewModel.refreshSurveys() // Fill the sample list

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
        surveysViewModel.refreshSurveys() // Fill the sample list
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

    @Test
    fun `When logging out from server failed, it triggers a LogoutError`() {
        // Arrange
        whenever(
            mockFullLogoutCompletableUseCase.execute(any())
        ) doReturn Completable.error(SessionError.LogoutError())

        // Act
        val errorObserver = surveysViewModel
            .error
            .test()
        surveysViewModel.logout()

        // Assert
        errorObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { it is SessionError.LogoutError }
    }

    @Test
    fun `When logging out from server successfully, it triggers to navigate to Onboarding screen`() {
        // Arrange
        whenever(
            mockFullLogoutCompletableUseCase.execute(any())
        ) doReturn Completable.complete()

        // Act
        val navigatorObserver = surveysViewModel
            .navigator
            .test()
        surveysViewModel.logout()

        // Assert
        navigatorObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { it is NavigationEvent.Surveys.Onboarding }
    }
}
