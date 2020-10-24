package co.nimblehq.usecase.survey

import co.nimblehq.data.error.SurveyError.GetSurveyDetailsError
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.data.model.Survey
import co.nimblehq.data.repository.SurveyRepository
import co.nimblehq.usecase.base.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class LoadSurveyDetailsSingleUseCase @Inject constructor(
    rxSchedulerProvider: RxSchedulerProvider,
    private val surveyRepository: SurveyRepository
) : SingleUseCase<LoadSurveyDetailsSingleUseCase.Input, Survey>(
    rxSchedulerProvider.io(),
    rxSchedulerProvider.main(),
    ::GetSurveyDetailsError
) {

    data class Input(
        val surveyId: String
    )

    override fun create(input: Input): Single<Survey> {
        return with(input) {
            surveyRepository.loadSurveyDetails(
                surveyId
            )
        }
    }
}
