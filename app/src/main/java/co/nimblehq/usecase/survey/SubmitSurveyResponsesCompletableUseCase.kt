package co.nimblehq.usecase.survey

import co.nimblehq.data.error.SurveyError.SubmitSurveyResponsesError
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.data.model.QuestionResponsesEntity
import co.nimblehq.data.model.toQuestionRequests
import co.nimblehq.data.repository.SurveyRepository
import co.nimblehq.usecase.base.CompletableUseCase
import io.reactivex.Completable
import javax.inject.Inject

class SubmitSurveyResponsesCompletableUseCase @Inject constructor(
    rxSchedulerProvider: RxSchedulerProvider,
    private val surveyRepository: SurveyRepository
) : CompletableUseCase<SubmitSurveyResponsesCompletableUseCase.Input>(
    rxSchedulerProvider.io(),
    rxSchedulerProvider.main(),
    ::SubmitSurveyResponsesError
) {

    data class Input(
        val surveyId: String,
        val responses: List<QuestionResponsesEntity>
    )

    override fun create(input: Input): Completable {
        return with(input) {
            surveyRepository.submitSurveyResponses(
                surveyId,
                responses.toQuestionRequests()
            )
        }
    }
}
