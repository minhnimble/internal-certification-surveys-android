package co.nimblehq.usecase.survey

import co.nimblehq.data.error.SurveyError.DeleteLocalSurveysListError
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.data.storage.dao.SurveyDao
import co.nimblehq.usecase.base.CompletableUseCase
import io.reactivex.Completable
import javax.inject.Inject

class DeleteLocalSurveysCompletableUseCase @Inject constructor(
    rxSchedulerProvider: RxSchedulerProvider,
    private val surveysDao: SurveyDao
) : CompletableUseCase<Unit>(
    rxSchedulerProvider.io(),
    rxSchedulerProvider.main(),
    ::DeleteLocalSurveysListError
) {

    override fun create(input: Unit): Completable {
        return Completable.fromAction {
            surveysDao.deleteSurvey()
        }
    }
}
