package co.nimblehq.ui.screen.main.surveys

import co.nimblehq.data.error.SurveyError
import co.nimblehq.data.lib.common.DEFAULT_INITIAL_SURVEYS_PAGE_NUMBER
import co.nimblehq.data.lib.common.DEFAULT_SURVEYS_PAGE_SIZE
import co.nimblehq.data.lib.common.DEFAULT_UNSELECTED_INDEX
import co.nimblehq.event.NavigationEvent
import co.nimblehq.extension.isValidIndex
import co.nimblehq.ui.base.BaseViewModel
import co.nimblehq.usecase.session.FullLogoutCompletableUseCase
import co.nimblehq.usecase.survey.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

interface Input {

    fun nextIndex()

    fun previousIndex()
}

interface Output {

    val error: Observable<Throwable>

    val navigator: Observable<NavigationEvent>

    val showLoading: Observable<Boolean>

    val showRefreshing: Observable<Boolean>

    val selectedSurveyIndex: Observable<Int>

    val surveyItemUiModels: Observable<List<SurveyItemUiModel>>

    val selectedSurveyIndexValue: Int

    val selectedSurveyUiModel: SurveyItemUiModel?
}

@HiltViewModel
class SurveysViewModel @Inject constructor(
    private val deleteLocalSurveysExcludeIdsCompletableUseCase: DeleteLocalSurveysExcludeIdsCompletableUseCase,
    private val fullLogoutCompletableUseCase: FullLogoutCompletableUseCase,
    private val getLocalSurveysSingleUseCase: GetLocalSurveysSingleUseCase,
    private val getSurveysCurrentPageSingleUseCase: GetSurveysCurrentPageSingleUseCase,
    private val getSurveysTotalPagesSingleUseCase: GetSurveysTotalPagesSingleUseCase,
    private val loadSurveysSingleUseCase: LoadSurveysSingleUseCase
) : BaseViewModel(), Input, Output {

    val input = this

    val output = this

    private val _error = PublishSubject.create<Throwable>()
    override val error: Observable<Throwable>
        get() = _error

    private val _navigator = PublishSubject.create<NavigationEvent>()
    override val navigator: Observable<NavigationEvent>
        get() = _navigator

    private val _showLoading = BehaviorSubject.create<Boolean>()
    override val showLoading: Observable<Boolean>
        get() = _showLoading

    private val _showRefreshing = BehaviorSubject.create<Boolean>()
    override val showRefreshing: Observable<Boolean>
        get() = _showRefreshing

    private val _selectedSurveyIndex = BehaviorSubject.createDefault(DEFAULT_UNSELECTED_INDEX)
    override val selectedSurveyIndex: Observable<Int>
        get() = _selectedSurveyIndex

    private val _surveyItemUiModels = BehaviorSubject.create<List<SurveyItemUiModel>>()
    override val surveyItemUiModels: Observable<List<SurveyItemUiModel>>
        get() = _surveyItemUiModels

    override val selectedSurveyIndexValue: Int
        get() = _selectedSurveyIndex.value ?: DEFAULT_UNSELECTED_INDEX

    override val selectedSurveyUiModel: SurveyItemUiModel?
        get() = currentSurveyItemUiModels.elementAtOrNull(selectedSurveyIndexValue)

    private val currentSurveyItemUiModels: List<SurveyItemUiModel>
        get() = _surveyItemUiModels.value.orEmpty()

    private var currentSurveysPageNumber = DEFAULT_INITIAL_SURVEYS_PAGE_NUMBER

    private val currentSurveysPageSize = DEFAULT_SURVEYS_PAGE_SIZE

    private var reachedLastSurveysPage = false

    override fun nextIndex() {
        val nextIndex = selectedSurveyIndexValue + 1
        updateSurveyIndex(nextIndex)

        if (shouldLoadMoreSurveys(nextIndex)) { // Load more if needed
            loadMoreSurveys()
        }
    }

    override fun previousIndex() {
        updateSurveyIndex(selectedSurveyIndexValue - 1)
    }

    fun getLocalCachedSurveys() {
        getLocalSurveysSingleUseCase
            .execute(Unit)
            .map { it.toSurveyItemUiModels() }
            .doFinally { _showLoading.onNext(false) }
            .subscribeBy(
                onSuccess = { bindOnSuccessGetLocalCachedSurveys(it) },
                onError = { _error.onNext(it) }
            )
            .bindForDisposable()
    }

    fun logout() {
        fullLogoutCompletableUseCase
            .execute(Unit)
            .doOnSubscribe { _showLoading.onNext(true) }
            .doFinally { _showLoading.onNext(false) }
            .subscribeBy(
                onComplete = { _navigator.onNext(NavigationEvent.Surveys.Onboarding) },
                onError = { _error.onNext(it) }
            )
            .bindForDisposable()
    }

    fun refreshSurveys() {
        loadInitialSurveys(isRefreshing = true)
    }

    private fun bindOnSuccessGetLocalCachedSurveys(surveys: List<SurveyItemUiModel>) {
        bindSurveysItemUiModels(surveys)
        getLocalPagingAttributes()
        loadInitialSurveys()
    }

    private fun bindOnSuccessLoadInitialSurveys(surveys: List<SurveyItemUiModel>) {
        bindSurveysItemUiModels(surveys, shouldMerge = false)
        getLocalPagingAttributes()
        deleteLocalCachedSurveys()
    }

    private fun bindOnSuccessLoadMoreSurveys(surveys: List<SurveyItemUiModel>) {
        bindSurveysItemUiModels(surveys, shouldResetToFirstIndex = false)
        getLocalPagingAttributes()
    }

    private fun bindSurveysItemUiModels(
        surveys: List<SurveyItemUiModel>,
        shouldMerge: Boolean = true,
        shouldResetToFirstIndex: Boolean = true
    ) {
        var finalSurveys = surveys
        if (shouldMerge) {
            finalSurveys = mergeSurveys(surveys)
        }
        _surveyItemUiModels.onNext(finalSurveys)
        if (shouldResetToFirstIndex) {
            updateSurveyIndex(0)
        }
    }

    private fun deleteLocalCachedSurveys() {
        deleteLocalSurveysExcludeIdsCompletableUseCase
            .execute(DeleteLocalSurveysExcludeIdsCompletableUseCase.Input(currentSurveyItemUiModels.map { model -> model.id }))
            .subscribeBy(
                onError = { _error.onNext(it) }
            )
            .bindForDisposable()
    }

    private fun getLocalPagingAttributes() {
        getSurveysCurrentPageSingleUseCase
            .execute(Unit)
            .flatMap { page ->
                currentSurveysPageNumber = page.coerceAtLeast(DEFAULT_INITIAL_SURVEYS_PAGE_NUMBER)
                getSurveysTotalPagesSingleUseCase.execute(Unit)
            }
            .subscribeBy(
                onSuccess = { reachedLastSurveysPage = it == currentSurveysPageNumber },
                onError = { _error.onNext(it) }
            )
            .bindForDisposable()
    }

    private fun loadInitialSurveys(isRefreshing: Boolean = false) {
        loadSurveysSingleUseCase
            .execute(LoadSurveysSingleUseCase.Input(DEFAULT_INITIAL_SURVEYS_PAGE_NUMBER, currentSurveysPageSize))
            .map { it.toSurveyItemUiModels() }
            .doOnSubscribe { (if (isRefreshing) _showRefreshing else _showLoading).onNext(true) }
            .doFinally { (if (isRefreshing) _showRefreshing else _showLoading).onNext(false) }
            .subscribeBy(
                onSuccess = { bindOnSuccessLoadInitialSurveys(it) },
                onError = { _error.onNext(it) }
            )
            .bindForDisposable()
    }

    private fun loadMoreSurveys() {
        loadSurveysSingleUseCase
            .execute(LoadSurveysSingleUseCase.Input(currentSurveysPageNumber + 1, currentSurveysPageSize)) // Load the next page by adding 1 to the current page
            .doOnSubscribe { _showLoading.onNext(true) }
            .map { it.toSurveyItemUiModels() }
            .doFinally { _showLoading.onNext(false) }
            .subscribeBy(
                onSuccess = { bindOnSuccessLoadMoreSurveys(it) },
                onError = { _error.onNext(it) }
            )
            .bindForDisposable()
    }

    private fun mergeSurveys(newSurveys: List<SurveyItemUiModel>): List<SurveyItemUiModel> =
        currentSurveyItemUiModels.plus(newSurveys).distinctBy { it.id }

    private fun shouldLoadMoreSurveys(newIndex: Int): Boolean {
        val lastIndex =  currentSurveyItemUiModels.lastIndex
        return if (lastIndex == DEFAULT_UNSELECTED_INDEX) {
            false
        } else {
            val extendedLastIndex = lastIndex + 1

            // Notify user when there is nothing more to load
            if (newIndex == extendedLastIndex && reachedLastSurveysPage) {
                _error.onNext(SurveyError.NoMoreSurveysError(null))
            }
            newIndex in (lastIndex..extendedLastIndex) && !reachedLastSurveysPage
        }
    }

    private fun updateSurveyIndex(newIndex: Int) {
        if (currentSurveyItemUiModels.isValidIndex(newIndex)) {
            _selectedSurveyIndex.onNext(newIndex)
        }
    }
}
