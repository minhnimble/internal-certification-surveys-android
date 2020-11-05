package co.nimblehq.usecase.survey

import co.nimblehq.data.error.SurveyError.GetSurveyDetailsError
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.data.model.Question
import co.nimblehq.data.repository.SurveyRepository
import co.nimblehq.usecase.base.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class LoadSurveyDetailsSingleUseCase @Inject constructor(
    rxSchedulerProvider: RxSchedulerProvider,
    private val surveyRepository: SurveyRepository
) : SingleUseCase<String, List<Question>>(
    rxSchedulerProvider.io(),
    rxSchedulerProvider.main(),
    ::GetSurveyDetailsError
) {

    override fun create(input: String): Single<List<Question>> {
        return surveyRepository.loadSurveyDetails(input)
    }
}
