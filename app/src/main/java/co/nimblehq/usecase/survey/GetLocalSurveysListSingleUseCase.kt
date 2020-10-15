package co.nimblehq.usecase.survey

import co.nimblehq.data.error.SurveyError.GetSurveysListError
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.data.model.Survey
import co.nimblehq.data.repository.SurveyRepository
import co.nimblehq.data.storage.dao.SurveyDao
import co.nimblehq.usecase.base.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class GetLocalSurveysListSingleUseCase @Inject constructor(
    rxSchedulerProvider: RxSchedulerProvider,
    private val surveyDao: SurveyDao
) : SingleUseCase<Unit, List<Survey>>(
    rxSchedulerProvider.io(),
    rxSchedulerProvider.main(),
    ::GetSurveysListError
) {

    override fun create(input: Unit): Single<List<Survey>> {
        return surveyDao.getAllSurveys()
    }
}
