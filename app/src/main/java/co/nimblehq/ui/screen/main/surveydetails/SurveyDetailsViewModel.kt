package co.nimblehq.ui.screen.main.surveydetails

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.ui.base.BaseViewModel
import co.nimblehq.ui.screen.main.surveys.toQuestionItemPagerUiModels
import co.nimblehq.usecase.survey.LoadSurveyDetailsSingleUseCase
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface Inputs { }

class SurveyDetailsViewModel @ViewModelInject constructor(
    private val loadSurveyDetailsSingleUseCase: LoadSurveyDetailsSingleUseCase
) : BaseViewModel(), Inputs {

    private val _getSurveyDetailsError = PublishSubject.create<Throwable>()

    private val _showLoading = BehaviorSubject.create<Boolean>()

    private val _questionItemPagerUiModels = BehaviorSubject.create<List<QuestionItemPagerUiModel>>()

    val getSurveyDetailsError: Observable<Throwable>
        get() = _getSurveyDetailsError

    val showLoading: Observable<Boolean>
        get() = _showLoading

    val questionItemPagerUiModels: Observable<List<QuestionItemPagerUiModel>>
        get() = _questionItemPagerUiModels

    val inputs: Inputs = this

    fun loadSurveyDetails(surveyId: String) {
        loadSurveyDetailsSingleUseCase
            .execute(LoadSurveyDetailsSingleUseCase.Input(surveyId))
            .doOnSubscribe { _showLoading.onNext(true) }
            .doFinally { _showLoading.onNext(false) }
            .subscribeBy (
                onSuccess = { _questionItemPagerUiModels.onNext(it.toQuestionItemPagerUiModels()) },
                onError = { _getSurveyDetailsError.onNext(it) }
            )
            .bindForDisposable()
    }
}

