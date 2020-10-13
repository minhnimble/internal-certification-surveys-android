package co.nimblehq.ui.screen.main.surveys

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.event.NavigationEvent
import co.nimblehq.ui.base.BaseViewModel
import co.nimblehq.ui.screen.main.surveys.adapter.SurveysPagerItemUiModel
import co.nimblehq.ui.screen.main.surveys.adapter.toSurveysPagerItemUiModel
import co.nimblehq.usecase.session.LoginByPasswordSingleUseCase
import co.nimblehq.usecase.survey.GetSurveysListSingleUseCase
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface Inputs { }

class SurveysViewModel @ViewModelInject constructor(
    private val getSurveysListSingleUseCase: GetSurveysListSingleUseCase
) : BaseViewModel(), Inputs {


    private val _error = PublishSubject.create<Throwable>()

    private val _showLoading = BehaviorSubject.create<Boolean>()

    private val _surveysPagerItemUiModels = BehaviorSubject.create<List<SurveysPagerItemUiModel>>()

    val error: Observable<Throwable>
        get() = _error

    val inputs: Inputs = this

    val showLoading: Observable<Boolean>
        get() = _showLoading

    val surveysPagerItemUiModels: Observable<List<SurveysPagerItemUiModel>>
        get() = _surveysPagerItemUiModels

    fun getSurveysList(pageNumber: Int = 0, pageSize: Int = 10) {
        getSurveysListSingleUseCase
            .execute(GetSurveysListSingleUseCase.Input(pageNumber, pageSize))
            .doFinally { _showLoading.onNext(false) }
            .subscribeBy(
                onSuccess = {
                    _surveysPagerItemUiModels.onNext(it.map { survey -> survey.toSurveysPagerItemUiModel() })
                    _showLoading.onNext(false)
                },
                onError = { _error.onNext(it) }
            )
            .bindForDisposable()
    }
}

