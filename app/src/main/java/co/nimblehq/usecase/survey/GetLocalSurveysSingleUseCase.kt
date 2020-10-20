package co.nimblehq.usecase.survey

import co.nimblehq.data.error.SurveyError.GetSurveysError
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.data.model.Survey
import co.nimblehq.data.storage.dao.SurveyDao
import co.nimblehq.usecase.base.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class GetLocalSurveysSingleUseCase @Inject constructor(
    rxSchedulerProvider: RxSchedulerProvider,
    private val surveyDao: SurveyDao
) : SingleUseCase<Unit, List<Survey>>(
    rxSchedulerProvider.io(),
    rxSchedulerProvider.main(),
    ::GetSurveysError
) {

    override fun create(input: Unit): Single<List<Survey>> {
        return surveyDao.getAllSurveys()
    }
}
