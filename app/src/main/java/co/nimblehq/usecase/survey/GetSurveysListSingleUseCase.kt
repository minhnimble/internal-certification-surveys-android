package co.nimblehq.usecase.survey

import co.nimblehq.data.error.SurveyError.GetSurveysListError
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.data.model.Survey
import co.nimblehq.data.repository.SurveyRepository
import co.nimblehq.usecase.base.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class GetSurveysListSingleUseCase @Inject constructor(
    rxSchedulerProvider: RxSchedulerProvider,
    private val surveyRepository: SurveyRepository
) : SingleUseCase<GetSurveysListSingleUseCase.Input, List<Survey>>(
    rxSchedulerProvider.io(),
    rxSchedulerProvider.main(),
    ::GetSurveysListError
) {

    data class Input(
        val pageNumber: Int,
        val pageSize: Int
    )

    override fun create(input: Input): Single<List<Survey>> {
        return with(input) {
            surveyRepository.getSurveysList(
                pageNumber,
                pageSize
            )
        }
    }
}
