package co.nimblehq.usecase.survey

import co.nimblehq.data.error.Ignored
import co.nimblehq.data.lib.common.DEFAULT_INITIAL_SURVEYS_PAGE_NUMBER
import co.nimblehq.data.lib.schedulers.RxSchedulerProvider
import co.nimblehq.data.storage.AppPreferences
import co.nimblehq.usecase.base.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class GetSurveysCurrentPageSingleUseCase @Inject constructor(
    rxSchedulerProvider: RxSchedulerProvider,
    private val appPreferences: AppPreferences
) : SingleUseCase<Unit, Int>(
    rxSchedulerProvider.io(),
    rxSchedulerProvider.main(),
    ::Ignored
) {

    override fun create(input: Unit): Single<Int> {
        return Single.just(appPreferences.surveysCurrentPage ?: DEFAULT_INITIAL_SURVEYS_PAGE_NUMBER)
    }
}
