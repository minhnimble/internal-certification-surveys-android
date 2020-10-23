package co.nimblehq.usecase.survey

import co.nimblehq.data.error.SurveyError.DeleteLocalSurveysError
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.data.storage.dao.SurveyDao
import co.nimblehq.usecase.base.CompletableUseCase
import io.reactivex.Completable
import javax.inject.Inject

class DeleteLocalSurveysExcludeIdsCompletableUseCase @Inject constructor(
    rxSchedulerProvider: RxSchedulerProvider,
    private val surveysDao: SurveyDao
) : CompletableUseCase<DeleteLocalSurveysExcludeIdsCompletableUseCase.Input>(
    rxSchedulerProvider.io(),
    rxSchedulerProvider.main(),
    ::DeleteLocalSurveysError
) {
    data class Input(
        val excludedIds: List<String>
    )

    override fun create(input: Input): Completable {
        return Completable.fromAction {
            surveysDao.deleteSurveys(
                excludedIds = input.excludedIds
            )
        }
    }
}
