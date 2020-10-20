package co.nimblehq.usecase.survey

import co.nimblehq.data.error.Ignored
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.data.storage.AppPreferences
import co.nimblehq.usecase.base.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class GetSurveysTotalPagesSingleUseCase @Inject constructor(
    rxSchedulerProvider: RxSchedulerProvider,
    private val appPreferences: AppPreferences
) : SingleUseCase<Unit, Int>(
    rxSchedulerProvider.io(),
    rxSchedulerProvider.main(),
    ::Ignored
) {

    override fun create(input: Unit): Single<Int> {
        return Single.just(appPreferences.surveysTotalPages)
    }
}
