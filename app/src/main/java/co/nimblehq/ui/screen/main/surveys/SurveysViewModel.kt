package co.nimblehq.ui.screen.main.surveys

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.data.lib.common.DEFAULT_INITIAL_SURVEYS_PAGE_NUMBER
import co.nimblehq.data.lib.common.DEFAULT_SURVEYS_PAGE_SIZE
import co.nimblehq.ui.base.BaseViewModel
import co.nimblehq.ui.screen.main.surveys.adapter.SurveysPagerItemUiModel
import co.nimblehq.ui.screen.main.surveys.adapter.toSurveysPagerItemUiModel
import co.nimblehq.usecase.survey.GetLocalSurveysListSingleUseCase
import co.nimblehq.usecase.survey.LoadSurveysListSingleUseCase
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface Inputs { }

class SurveysViewModel @ViewModelInject constructor(
    private val getLocalSurveysListSingleUseCase: GetLocalSurveysListSingleUseCase,
    private val loadSurveysListSingleUseCase: LoadSurveysListSingleUseCase
) : BaseViewModel(), Inputs {

    private val _error = PublishSubject.create<Throwable>()

    private val _showLoading = BehaviorSubject.create<Boolean>()

    private val _surveysPagerItemUiModels = BehaviorSubject.create<List<SurveysPagerItemUiModel>>()

    val error: Observable<Throwable>
        get() = _error

    val showLoading: Observable<Boolean>
        get() = _showLoading

    val surveysPagerItemUiModels: Observable<List<SurveysPagerItemUiModel>>
        get() = _surveysPagerItemUiModels

    fun checkAndLoadSurveysListIfNeeded() {
        getLocalSurveysListSingleUseCase
            .execute(Unit)
            .map { it.map { survey -> survey.toSurveysPagerItemUiModel() } }
            .subscribeBy(
                onSuccess = {
                    if (it.isEmpty()) {
                        loadSurveysList()
                    } else {
                        _showLoading.onNext(false)
                        _surveysPagerItemUiModels.onNext(it)
                    }
                },
                onError = {
                    _showLoading.onNext(false)
                    _error.onNext(it)
                }
            )
            .bindForDisposable()
    }

    private fun loadSurveysList(pageNumber: Int = DEFAULT_INITIAL_SURVEYS_PAGE_NUMBER, pageSize: Int = DEFAULT_SURVEYS_PAGE_SIZE) {
        loadSurveysListSingleUseCase
            .execute(LoadSurveysListSingleUseCase.Input(pageNumber, pageSize))
            .map { it.map { survey -> survey.toSurveysPagerItemUiModel() } }
            .doFinally { _showLoading.onNext(false) }
            .subscribeBy(
                onSuccess = { _surveysPagerItemUiModels.onNext(it) },
                onError = { _error.onNext(it) }
            )
            .bindForDisposable()
    }
}

