package co.nimblehq.ui.screen.main.surveydetails

import co.nimblehq.data.lib.common.DEFAULT_UNSELECTED_INDEX
import co.nimblehq.data.model.QuestionResponsesEntity
import co.nimblehq.extension.isValidIndex
import co.nimblehq.ui.base.BaseViewModel
import co.nimblehq.ui.screen.main.surveydetails.uimodel.QuestionItemPagerUiModel
import co.nimblehq.ui.screen.main.surveydetails.uimodel.toQuestionItemPagerUiModels
import co.nimblehq.usecase.survey.LoadSurveyDetailsSingleUseCase
import co.nimblehq.usecase.survey.SubmitSurveyResponsesCompletableUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

interface Input {

    fun currentQuestionIndex(index: Int)

    fun triggerNextQuestion()
}

interface Output {

    val showError: Observable<Throwable>

    val showLoading: Observable<Boolean>

    val showSuccessOverlay: Observable<Boolean>

    val currentQuestionIndex: Observable<Int>

    val questionItemPagerUiModels: Observable<List<QuestionItemPagerUiModel>>

    val reachedLastQuestion: Observable<Boolean>

    val questionIndicator: Observable<String>
}

@HiltViewModel
class SurveyDetailsViewModel @Inject constructor(
    private val loadSurveyDetailsSingleUseCase: LoadSurveyDetailsSingleUseCase,
    private val submitSurveyResponsesCompletableUseCase: SubmitSurveyResponsesCompletableUseCase
) : BaseViewModel(), Input, Output {

    val input = this

    val output = this

    private val _showError = PublishSubject.create<Throwable>()
    override val showError: Observable<Throwable>
        get() = _showError

    private val _showLoading = BehaviorSubject.create<Boolean>()
    override val showLoading: Observable<Boolean>
        get() = _showLoading

    private val _showSuccessOverlay = PublishSubject.create<Boolean>()
    override val showSuccessOverlay: Observable<Boolean>
        get() = _showSuccessOverlay

    private val _currentQuestionIndex = BehaviorSubject.create<Int>()
    override val currentQuestionIndex: Observable<Int>
        get() = _currentQuestionIndex

    private val _questionItemPagerUiModels = BehaviorSubject.create<List<QuestionItemPagerUiModel>>()
    override val questionItemPagerUiModels: Observable<List<QuestionItemPagerUiModel>>
        get() = _questionItemPagerUiModels

    override val reachedLastQuestion: Observable<Boolean>
        get() = Observables.combineLatest(
            _currentQuestionIndex,
            _questionItemPagerUiModels
        ) { selectedIndex, questions ->
            selectedIndex == questions.size - 1
        }

    override val questionIndicator: Observable<String>
        get() = Observables.combineLatest(
            _currentQuestionIndex,
            _questionItemPagerUiModels
        ) { selectedIndex, questions ->
            "${selectedIndex + 1}/${questions.size}"
        }

    private val currentQuestionIndexValue: Int
        get() = _currentQuestionIndex.value ?: DEFAULT_UNSELECTED_INDEX

    private val currentQuestionItemPagerUiModels: List<QuestionItemPagerUiModel>
        get() = _questionItemPagerUiModels.value.orEmpty()

    override fun currentQuestionIndex(index: Int) {
        _currentQuestionIndex.onNext(index)
    }

    override fun triggerNextQuestion() {
        val nextIndex = currentQuestionIndexValue + 1
        if (currentQuestionItemPagerUiModels.isValidIndex(nextIndex)) {
            _currentQuestionIndex.onNext(nextIndex)
        }
    }

    fun loadSurveyDetails(surveyId: String) {
        loadSurveyDetailsSingleUseCase
            .execute(surveyId)
            .doOnSubscribe { _showLoading.onNext(true) }
            .doFinally { _showLoading.onNext(false) }
            .subscribeBy(
                onSuccess = { _questionItemPagerUiModels.onNext(it.toQuestionItemPagerUiModels()) },
                onError = { _showError.onNext(it) }
            )
            .bindForDisposable()
    }

    fun submitSurveyResponses(surveyId: String, responses: List<QuestionResponsesEntity>) {
        submitSurveyResponsesCompletableUseCase
            .execute(SubmitSurveyResponsesCompletableUseCase.Input(surveyId, responses))
            .doOnSubscribe { _showLoading.onNext(true) }
            .doFinally { _showLoading.onNext(false) }
            .subscribeBy (
                onComplete = { _showSuccessOverlay.onNext(true) },
                onError = { _showError.onNext(it) }
            )
            .bindForDisposable()
    }
}
