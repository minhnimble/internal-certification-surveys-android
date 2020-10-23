package co.nimblehq.ui.screen.main.surveys

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.data.error.SurveyError
import co.nimblehq.data.lib.common.DEFAULT_INITIAL_SURVEYS_PAGE_NUMBER
import co.nimblehq.data.lib.common.DEFAULT_SURVEYS_PAGE_SIZE
import co.nimblehq.data.lib.common.DEFAULT_UNSELECTED_INDEX
import co.nimblehq.extension.isValidIndex
import co.nimblehq.ui.base.BaseViewModel
import co.nimblehq.usecase.survey.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface Inputs {

    fun nextIndex()

    fun previousIndex()
}

class SurveysViewModel @ViewModelInject constructor(
    private val deleteLocalSurveysExcludeIdsCompletableUseCase: DeleteLocalSurveysExcludeIdsCompletableUseCase,
    private val getLocalSurveysSingleUseCase: GetLocalSurveysSingleUseCase,
    private val getSurveysCurrentPageSingleUseCase: GetSurveysCurrentPageSingleUseCase,
    private val getSurveysTotalPagesSingleUseCase: GetSurveysTotalPagesSingleUseCase,
    private val loadSurveysSingleUseCase: LoadSurveysSingleUseCase
) : BaseViewModel(), Inputs {

    private val _error = PublishSubject.create<Throwable>()

    private val _showLoading = BehaviorSubject.create<Boolean>()

    private val _showRefreshing = BehaviorSubject.create<Boolean>()

    private val _selectedSurveyIndex = BehaviorSubject.createDefault(DEFAULT_UNSELECTED_INDEX)

    private val _surveyItemUiModels = BehaviorSubject.create<List<SurveyItemUiModel>>()

    val error: Observable<Throwable>
        get() = _error

    val showLoading: Observable<Boolean>
        get() = _showLoading

    val showRefreshing: Observable<Boolean>
        get() = _showRefreshing

    val selectedSurveyIndex: Observable<Int>
        get() = _selectedSurveyIndex

    val surveyItemUiModels: Observable<List<SurveyItemUiModel>>
        get() = _surveyItemUiModels

    val inputs: Inputs = this

    val selectedSurveyIndexValue: Int
        get() = _selectedSurveyIndex.value ?: DEFAULT_UNSELECTED_INDEX

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

    fun refreshSurveysList() {
        loadSurveysSingleUseCase
            .execute(LoadSurveysSingleUseCase.Input(DEFAULT_INITIAL_SURVEYS_PAGE_NUMBER, currentSurveysPageSize))
            .doOnSubscribe { _showRefreshing.onNext(true) }
            .map { it.toSurveyItemUiModels() }
            .doOnSuccess { bindOnSuccessLoadSurveys(it, shouldMerge = false) }
            .flatMap { getLocalPagingAttributes() }
            .flatMapCompletable {
                deleteLocalSurveysExcludeIdsCompletableUseCase
                    .execute(DeleteLocalSurveysExcludeIdsCompletableUseCase.Input(currentSurveyItemUiModels.map { model -> model.id }))
            }
            .doFinally { _showRefreshing.onNext(false) }
            .subscribeBy(
                onError = { _error.onNext(it) }
            )
            .bindForDisposable()
    }

    fun checkAndRefreshInitialSurveys() {
        getLocalSurveysSingleUseCase
            .execute(Unit)
            .map { it.toSurveyItemUiModels() }
            .doOnSuccess { bindOnSuccessLoadSurveys(it) }
            .flatMap { getLocalPagingAttributes() }
            .flatMap { loadSurveysSingleUseCase.execute(LoadSurveysSingleUseCase.Input(DEFAULT_INITIAL_SURVEYS_PAGE_NUMBER, currentSurveysPageSize)) }
            .map { it.toSurveyItemUiModels() }
            .doOnSuccess { bindOnSuccessLoadSurveys(it, shouldMerge = false) }
            .flatMap { getLocalPagingAttributes() }
            .flatMapCompletable {
                deleteLocalSurveysExcludeIdsCompletableUseCase
                    .execute(DeleteLocalSurveysExcludeIdsCompletableUseCase.Input(currentSurveyItemUiModels.map { model -> model.id }))
            }
            .doFinally { _showLoading.onNext(false) }
            .subscribeBy(
                onError = { _error.onNext(it) }
            )
            .bindForDisposable()
    }

    fun getSelectedSurveyUiModel(): SurveyItemUiModel? {
        val models = currentSurveyItemUiModels
        val selectedIndex = selectedSurveyIndexValue
        return models.elementAtOrNull(selectedIndex)
    }

    private fun bindOnSuccessLoadSurveys(
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

    private fun getLocalPagingAttributes(): Single<Int> {
        return getSurveysCurrentPageSingleUseCase.execute(Unit)
            .doOnSuccess { currentSurveysPageNumber = it.coerceAtLeast(DEFAULT_INITIAL_SURVEYS_PAGE_NUMBER) }
            .flatMap { getSurveysTotalPagesSingleUseCase.execute(Unit) }
            .doOnSuccess { reachedLastSurveysPage = it == currentSurveysPageNumber }
    }

    private fun loadMoreSurveys() {
        loadSurveysSingleUseCase
            .execute(LoadSurveysSingleUseCase.Input(currentSurveysPageNumber + 1, currentSurveysPageSize)) // Load the next page by adding 1 to the current page
            .doOnSubscribe { _showLoading.onNext(true) }
            .map { it.toSurveyItemUiModels() }
            .doOnSuccess { bindOnSuccessLoadSurveys(it, shouldResetToFirstIndex = false) }
            .flatMap { getLocalPagingAttributes() }
            .doFinally { _showLoading.onNext(false) }
            .subscribeBy(
                onError = { _error.onNext(it) }
            )
            .bindForDisposable()
    }

    private fun mergeSurveys(newSurveys: List<SurveyItemUiModel>): List<SurveyItemUiModel> =
        currentSurveyItemUiModels.plus(newSurveys).distinctBy { it.id }

    private fun updateSurveyIndex(newIndex: Int) {
        if (currentSurveyItemUiModels.isValidIndex(newIndex)) {
            _selectedSurveyIndex.onNext(newIndex)
        }
    }

    private fun shouldLoadMoreSurveys(newIndex: Int): Boolean {
        val lastIndex =  currentSurveyItemUiModels.lastIndex
        return if (lastIndex == -1) {
            false
        }
        else {
            // Notify user when there is nothing more to load
            if (newIndex == lastIndex + 1 && reachedLastSurveysPage) {
                _error.onNext(SurveyError.NoMoreSurveysError(null))
            }
            newIndex in (lastIndex..lastIndex + 1) && !reachedLastSurveysPage
        }
    }
}

