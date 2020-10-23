package co.nimblehq.ui.screen.main.surveys

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.data.error.Ignored
import co.nimblehq.data.error.SurveyError
import co.nimblehq.data.lib.common.DEFAULT_INITIAL_SURVEYS_PAGE_NUMBER
import co.nimblehq.data.lib.common.DEFAULT_SURVEYS_PAGE_SIZE
import co.nimblehq.data.lib.common.DEFAULT_UNSELECTED_INDEX
import co.nimblehq.extension.isNotValidIndex
import co.nimblehq.extension.isValidIndex
import co.nimblehq.ui.base.BaseViewModel
import co.nimblehq.usecase.survey.*
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface Inputs {

    fun nextIndex()

    fun previousIndex()
}

class SurveysViewModel @ViewModelInject constructor(
    private val deleteLocalSurveysCompletableUseCase: DeleteLocalSurveysCompletableUseCase,
    private val getLocalSurveysSingleUseCase: GetLocalSurveysSingleUseCase,
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

    private var currentSurveysPageSize = DEFAULT_SURVEYS_PAGE_SIZE

    private var reachedLastSurveysPage = false

    override fun nextIndex() {
        val nextIndex = selectedSurveyIndexValue + 1
        updateSurveyIndex(nextIndex)

        if (shouldLoadMoreSurveys(nextIndex)) { // When we reach the last item, trigger to load more if needed
            checkAndLoadMoreSurveysIfNeeded()
        }
    }

    override fun previousIndex() {
        updateSurveyIndex(selectedSurveyIndexValue - 1)
    }

    fun refreshSurveysList() {
        loadSurveysSingleUseCase
            .execute(LoadSurveysSingleUseCase.Input(DEFAULT_INITIAL_SURVEYS_PAGE_NUMBER, currentSurveysPageSize))
            .doOnSubscribe { _showRefreshing.onNext(true) }
            .map { it.map { survey -> survey.toSurveyItemUiModel() } }
            .doOnSuccess {
                bindOnSuccessLoadSurveys(it, shouldMerge = false)
                updateSurveyIndex(0)
            }
            .flatMapCompletable { deleteLocalSurveysCompletableUseCase.execute(DeleteLocalSurveysCompletableUseCase.Input(it.map { model -> model.id })) }
            .andThen(getSurveysTotalPagesSingleUseCase.execute(Unit))
            .doFinally { _showRefreshing.onNext(false) }
            .subscribeBy(
                onSuccess = { reachedLastSurveysPage = it in 1..currentSurveysPageNumber },
                onError = { _error.onNext(it) }
            )
            .bindForDisposable()
    }

    fun checkAndRefreshInitialSurveys() {
        getLocalSurveysSingleUseCase
            .execute(Unit)
            .map { it.map { survey -> survey.toSurveyItemUiModel() } }
            .doOnSuccess {
                bindOnSuccessLoadSurveys(it, shouldMerge = false)
                if (it.isNotEmpty()) {
                    _showLoading.onNext(false)
                    updateSurveyIndex(0)
                }
            }
            .flatMap { loadSurveysSingleUseCase.execute(LoadSurveysSingleUseCase.Input(DEFAULT_INITIAL_SURVEYS_PAGE_NUMBER, currentSurveysPageSize)) }
            .map { it.map { survey -> survey.toSurveyItemUiModel() } }
            .doOnSuccess {
                bindOnSuccessLoadSurveys(it)
                if (selectedSurveyIndexValue == DEFAULT_UNSELECTED_INDEX) updateSurveyIndex(0)
            }
            .flatMap { getSurveysTotalPagesSingleUseCase.execute(Unit) }
            .doFinally { _showLoading.onNext(false) }
            .subscribeBy(
                onSuccess = { reachedLastSurveysPage = it in 1..currentSurveysPageNumber },
                onError = { _error.onNext(it) }
            )
            .bindForDisposable()
    }

    fun getSelectedSurveyUiModel(): SurveyItemUiModel? {
        val models = currentSurveyItemUiModels
        val selectedIndex = selectedSurveyIndexValue
        return if (models.isValidIndex(selectedIndex)) {
            models[selectedIndex]
        } else {
            null
        }
    }

    private fun bindOnSuccessLoadSurveys(
        surveys: List<SurveyItemUiModel>,
        shouldMerge: Boolean = true,
        shouldUpdatePageNumber: Boolean = true,
    ) {
        var finalSurveys = surveys
        if (shouldMerge) {
            finalSurveys = mergeSurveys(surveys)
        }
        if (shouldUpdatePageNumber) {
            calculateSurveysPageNumber(finalSurveys.size)
        }
        _surveyItemUiModels.onNext(finalSurveys)
    }

    private fun calculateSurveysPageNumber(totalItems: Int) {
        var pageNumber = totalItems / currentSurveysPageSize
        if (pageNumber < DEFAULT_INITIAL_SURVEYS_PAGE_NUMBER) pageNumber = DEFAULT_INITIAL_SURVEYS_PAGE_NUMBER
        currentSurveysPageNumber = pageNumber
    }

    private fun checkAndLoadMoreSurveysIfNeeded() {
        if (reachedLastSurveysPage) {
            _error.onNext(SurveyError.NoMoreSurveysError(null))
            return
        }
        val pageShiftThreshold = 1
        currentSurveysPageNumber += pageShiftThreshold
        loadSurveysSingleUseCase
            .execute(LoadSurveysSingleUseCase.Input(currentSurveysPageNumber, currentSurveysPageSize))
            .doOnSubscribe { _showLoading.onNext(true) }
            .map { it.map { survey -> survey.toSurveyItemUiModel() } }
            .doOnSuccess { bindOnSuccessLoadSurveys(it, shouldUpdatePageNumber = false) }
            .flatMap { getSurveysTotalPagesSingleUseCase.execute(Unit) }
            .doFinally { _showLoading.onNext(false) }
            .subscribeBy(
                onSuccess = { reachedLastSurveysPage = it in 1..currentSurveysPageNumber },
                onError = { error ->
                    if (error !is Ignored) currentSurveysPageNumber -= pageShiftThreshold
                    _error.onNext(error)
                }
            )
            .bindForDisposable()
    }

    private fun mergeSurveys(newSurveys: List<SurveyItemUiModel>): List<SurveyItemUiModel> =
        currentSurveyItemUiModels.plus(newSurveys).distinctBy { it.id }

    private fun updateSurveyIndex(newIndex: Int) {
        if (currentSurveyItemUiModels.isNotValidIndex(newIndex)) return
        _selectedSurveyIndex.onNext(newIndex)
    }

    private fun shouldLoadMoreSurveys(index: Int): Boolean {
        val surveys = currentSurveyItemUiModels
        return surveys.size - 1 == index && !reachedLastSurveysPage || surveys.size == index
    }
}

