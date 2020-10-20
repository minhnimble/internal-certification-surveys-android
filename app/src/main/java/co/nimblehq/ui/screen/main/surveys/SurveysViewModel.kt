package co.nimblehq.ui.screen.main.surveys

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.data.error.SurveyError
import co.nimblehq.data.lib.common.DEFAULT_INITIAL_SURVEYS_PAGE_NUMBER
import co.nimblehq.data.lib.common.DEFAULT_SURVEYS_PAGE_SIZE
import co.nimblehq.data.lib.common.DEFAULT_UNSELECTED_INDEX
import co.nimblehq.ui.base.BaseViewModel
import co.nimblehq.ui.screen.main.surveys.adapter.SurveysPagerItemUiModel
import co.nimblehq.ui.screen.main.surveys.adapter.toSurveysPagerItemUiModel
import co.nimblehq.usecase.survey.DeleteLocalSurveysCompletableUseCase
import co.nimblehq.usecase.survey.GetInitialSurveysListFlowableUseCase
import co.nimblehq.usecase.survey.LoadMoreSurveysListSingleUseCase
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
    private val getInitialSurveysListFlowableUseCase: GetInitialSurveysListFlowableUseCase,
    private val loadMoreSurveysListSingleUseCase: LoadMoreSurveysListSingleUseCase
) : BaseViewModel(), Inputs {

    private val _error = PublishSubject.create<Throwable>()

    private val _showLoading = BehaviorSubject.create<Boolean>()

    private val _selectedSurveyItem = BehaviorSubject.create<SurveysPagerItemUiModel>()

    private val _selectedSurveyIndex = BehaviorSubject.createDefault(DEFAULT_UNSELECTED_INDEX)

    private val _surveysPagerItemUiModels = BehaviorSubject.create<List<SurveysPagerItemUiModel>>()

    val error: Observable<Throwable>
        get() = _error

    val showLoading: Observable<Boolean>
        get() = _showLoading

    val selectedSurveyItem: Observable<SurveysPagerItemUiModel?>
        get() = _selectedSurveyItem

    val selectedSurveyIndex: Observable<Int>
        get() = _selectedSurveyIndex

    val surveysPagerItemUiModels: Observable<List<SurveysPagerItemUiModel>>
        get() = _surveysPagerItemUiModels

    val inputs: Inputs = this

    val selectedSurveyIndexValue: Int
        get() = _selectedSurveyIndex.value ?: DEFAULT_UNSELECTED_INDEX

    private var activePageSize = DEFAULT_SURVEYS_PAGE_SIZE
    private var currentPageNumber = DEFAULT_INITIAL_SURVEYS_PAGE_NUMBER
    private var loadedLastPage = false

    override fun nextIndex() {
        _selectedSurveyIndex.value?.let {
            val surveyItems = _surveysPagerItemUiModels.value.orEmpty()
            when {
                it == DEFAULT_UNSELECTED_INDEX -> { // Selected index is not set yet
                    if (surveyItems.isNotEmpty()) {
                        _selectedSurveyIndex.onNext(0)
                        _selectedSurveyItem.onNext(surveyItems[0])
                    }
                }
                it >= 0 -> { // Selected index is already set before
                    val nextIndex = it + 1
                    if (surveyItems.size > nextIndex) {
                        _selectedSurveyIndex.onNext(nextIndex)
                        _selectedSurveyItem.onNext(surveyItems[nextIndex])
                    }

                    // When we reach the last item, trigger to load more
                    if (surveyItems.size - 1 == nextIndex && !loadedLastPage || surveyItems.size == nextIndex) {
                        loadMoreSurveysList()
                    }
                }
            }
        }
    }

    override fun previousIndex() {
        _selectedSurveyIndex.value?.let {
            if (it > 0) { // Selected index is already set before
                val previousIndex = it - 1
                _selectedSurveyIndex.onNext(previousIndex)
                _selectedSurveyItem.onNext(_surveysPagerItemUiModels.value.orEmpty()[previousIndex])
            }
        }
    }

    fun refreshSurveysList() {
        resetSurveysPagingAttributes()

        deleteLocalSurveysCompletableUseCase
            .execute(Unit)
            .doOnSubscribe { _showLoading.onNext(true) }
            .andThen(loadMoreSurveysListSingleUseCase.execute(LoadMoreSurveysListSingleUseCase.Input(currentPageNumber, activePageSize)))
            .map { it.map { survey -> survey.toSurveysPagerItemUiModel() } }
            .doFinally { _showLoading.onNext(false) }
            .subscribeBy(
                onSuccess = {
                    // Clear current displaying survey
                    _selectedSurveyIndex.onNext(DEFAULT_UNSELECTED_INDEX)
                    _selectedSurveyItem.onNext(SurveysPagerItemUiModel())

                    bindOnSuccessLoadSurveyItems(it, false)

                    if (it.isNotEmpty()) { // Reset displaying contents to first item if available
                        _selectedSurveyIndex.onNext(0)
                        _selectedSurveyItem.onNext(it[0])
                    }

                },
                onError = { bindOnErrorLoadSurveyItems(it) }
            )
            .bindForDisposable()
    }

    fun checkAndLoadInitialSurveysListIfNeeded() {
        getInitialSurveysListFlowableUseCase
            .execute(GetInitialSurveysListFlowableUseCase.Input(DEFAULT_INITIAL_SURVEYS_PAGE_NUMBER, activePageSize))
            .map { it.map { survey -> survey.toSurveysPagerItemUiModel() } }
            .doFinally { _showLoading.onNext(false) }
            .subscribeBy(
                onNext = {
                    if (it.isNotEmpty()) {
                        _showLoading.onNext(false)
                        if (_selectedSurveyIndex.value == DEFAULT_UNSELECTED_INDEX) {
                            _selectedSurveyIndex.onNext(0)
                            _selectedSurveyItem.onNext(it[0])
                        }
                    }
                    val finalSurveysList = mergeSurveysList(it)
                    calculateInitialPageNumber(finalSurveysList.size)
                    _surveysPagerItemUiModels.onNext(finalSurveysList)
                },
                onError = { _error.onNext(it) }
            )
            .bindForDisposable()
    }

    private fun calculateInitialPageNumber(totalItems: Int) {
        var pageNumber = totalItems / activePageSize
        if (pageNumber < 1) pageNumber = 1
        currentPageNumber = pageNumber

        if (totalItems % activePageSize != 0) {
            loadedLastPage = true
        }
    }

    private fun bindOnErrorLoadSurveyItems(error: Throwable) {
        _error.onNext(error)
        if ((error as? SurveyError.GetSurveysListError)?.isNotFound == true) {
            loadedLastPage = true
            _error.onNext(SurveyError.NoMoreSurveysListError(null))
        } else {
            _error.onNext(error)
        }
    }

    private fun bindOnSuccessLoadSurveyItems(items: List<SurveysPagerItemUiModel>, shouldMerge: Boolean = true) {
        if (items.isEmpty() || items.size % activePageSize != 0) {
            loadedLastPage = true
        }
        var finalItems = items
        if (shouldMerge) finalItems = mergeSurveysList(items)
        _surveysPagerItemUiModels.onNext(finalItems)
    }

    private fun loadMoreSurveysList() {
        if (loadedLastPage) {
            _error.onNext(SurveyError.NoMoreSurveysListError(null))
            return
        }
        currentPageNumber += 1
        loadMoreSurveysListSingleUseCase
            .execute(LoadMoreSurveysListSingleUseCase.Input(currentPageNumber, activePageSize))
            .doOnSubscribe { _showLoading.onNext(true) }
            .map { it.map { survey -> survey.toSurveysPagerItemUiModel() } }
            .doFinally { _showLoading.onNext(false) }
            .subscribeBy(
                onSuccess = { bindOnSuccessLoadSurveyItems(it) },
                onError = { bindOnErrorLoadSurveyItems(it) }
            )
            .bindForDisposable()
    }

    private fun mergeSurveysList(newList: List<SurveysPagerItemUiModel>): List<SurveysPagerItemUiModel> {
        return _surveysPagerItemUiModels.value?.let {
            it.plus(newList).distinctBy { survey -> survey.id }
        } ?: run { newList }
    }

    private fun resetSurveysPagingAttributes() {
        activePageSize = DEFAULT_SURVEYS_PAGE_SIZE
        currentPageNumber = DEFAULT_INITIAL_SURVEYS_PAGE_NUMBER
        loadedLastPage = false
    }
}

