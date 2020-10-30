package co.nimblehq.ui.screen.main.surveydetails

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.data.lib.common.DEFAULT_UNSELECTED_INDEX
import co.nimblehq.extension.isValidIndex
import co.nimblehq.ui.base.BaseViewModel
import co.nimblehq.ui.screen.main.surveydetails.uimodel.QuestionItemPagerUiModel
import co.nimblehq.ui.screen.main.surveydetails.uimodel.toQuestionItemPagerUiModels
import co.nimblehq.usecase.survey.LoadSurveyDetailsSingleUseCase
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface Inputs {
    fun currentQuestionIndex(index: Int)

    fun triggerNextQuestion()
}

class SurveyDetailsViewModel @ViewModelInject constructor(
    private val loadSurveyDetailsSingleUseCase: LoadSurveyDetailsSingleUseCase
) : BaseViewModel(), Inputs {

    private val _showError = PublishSubject.create<Throwable>()

    private val _showLoading = BehaviorSubject.create<Boolean>()

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
            "${(selectedIndex ?: DEFAULT_UNSELECTED_INDEX) + 1}/${questions.size}"
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
}

