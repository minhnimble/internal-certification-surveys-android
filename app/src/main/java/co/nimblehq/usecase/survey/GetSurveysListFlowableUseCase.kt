package co.nimblehq.usecase.survey

import co.nimblehq.data.error.SurveyError.GetSurveysListError
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.data.model.Survey
import co.nimblehq.data.repository.SurveyRepository
import co.nimblehq.usecase.base.FlowableUseCase
import io.reactivex.Flowable
import javax.inject.Inject

class GetSurveysListFlowableUseCase @Inject constructor(
    rxSchedulerProvider: RxSchedulerProvider,
    private val surveyRepository: SurveyRepository
) : FlowableUseCase<GetSurveysListFlowableUseCase.Input, List<Survey>>(
    rxSchedulerProvider.io(),
    rxSchedulerProvider.main(),
    ::GetSurveysListError
) {

    data class Input(
        val pageNumber: Int,
        val pageSize: Int
    )

    override fun create(input: Input): Flowable<List<Survey>> {
        return with(input) {
            surveyRepository.getSurveysList(
                pageNumber,
                pageSize
            )
        }
    }
}
