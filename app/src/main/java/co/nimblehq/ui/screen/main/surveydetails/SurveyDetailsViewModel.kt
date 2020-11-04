package co.nimblehq.ui.screen.main.surveydetails

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.data.lib.common.DEFAULT_UNSELECTED_INDEX
import co.nimblehq.data.model.QuestionResponsesEntity
import co.nimblehq.extension.isValidIndex
import co.nimblehq.ui.base.BaseViewModel
import co.nimblehq.ui.screen.main.surveydetails.uimodel.QuestionItemPagerUiModel
import co.nimblehq.ui.screen.main.surveydetails.uimodel.toQuestionItemPagerUiModels
import co.nimblehq.usecase.survey.LoadSurveyDetailsSingleUseCase
import co.nimblehq.usecase.survey.SubmitSurveyResponsesCompletableUseCase
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

interface Inputs {
    fun currentQuestionIndex(index: Int)

    fun triggerNextQuestion()
}

class SurveyDetailsViewModel @ViewModelInject constructor(
    private val loadSurveyDetailsSingleUseCase: LoadSurveyDetailsSingleUseCase,
    private val submitSurveyResponsesCompletableUseCase: SubmitSurveyResponsesCompletableUseCase
) : BaseViewModel(), Inputs {

    private val _showError = PublishSubject.create<Throwable>()

    private val _showLoading = BehaviorSubject.create<Boolean>()

    private val _showSuccessOverlay = PublishSubject.create<Boolean>()

    private val _currentQuestionIndex = BehaviorSubject.create<Int>()

    private val _questionItemPagerUiModels = BehaviorSubject.create<List<QuestionItemPagerUiModel>>()

    private val currentQuestionIndexValue: Int
        get() = _currentQuestionIndex.value ?: DEFAULT_UNSELECTED_INDEX

    private val currentQuestionItemPagerUiModels: List<QuestionItemPagerUiModel>
        get() = _questionItemPagerUiModels.value.orEmpty()

    val showError: Observable<Throwable>
        get() = _showError

    val showLoading: Observable<Boolean>
        get() = _showLoading

    val showSuccessOverlay: Observable<Boolean>
        get() = _showSuccessOverlay

    val reachedLastQuestion: Observable<Boolean>
        get() = Observables.combineLatest(
            _currentQuestionIndex,
            _questionItemPagerUiModels
        ) { selectedIndex, questions ->
            selectedIndex == questions.size - 1
        }

    val questionIndicator: Observable<String>
        get() = Observables.combineLatest(
            _currentQuestionIndex,
            _questionItemPagerUiModels
        ) { selectedIndex, questions ->
            "${selectedIndex + 1}/${questions.size}"
        }

    val currentQuestionIndex: Observable<Int>
        get() = _currentQuestionIndex

    val questionItemPagerUiModels: Observable<List<QuestionItemPagerUiModel>>
        get() = _questionItemPagerUiModels

    val inputs: Inputs = this

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
            // TODO: Update this flow to use lottie animation instead of delaying 3 seconds like now
            .doOnComplete { _showSuccessOverlay.onNext(true) }
            .doOnError { _showError.onNext(it) }
            .delay(3, TimeUnit.SECONDS)
            .subscribeBy (
                onComplete = { _showSuccessOverlay.onNext(false) }
            )
            .bindForDisposable()
    }
}

