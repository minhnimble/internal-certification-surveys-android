package co.nimblehq.usecase.base

import co.nimblehq.BuildConfig
import co.nimblehq.data.error.AppError
import co.nimblehq.data.lib.schedulers.RxScheduler
import io.reactivex.Completable

abstract class CompletableUseCase<in UseCaseInput>(
        private val executionThread: RxScheduler,
        private val postExecutionThread: RxScheduler,
        defaultError: (Throwable) -> AppError
) : BaseUseCase(defaultError) {

    protected abstract fun create(input: UseCaseInput): Completable

    fun execute(input: UseCaseInput): Completable {
        return create(input)
            .onErrorResumeNext {
                if (isCanceledException(it)) {
                    Completable.never()
                } else {
                    Completable.error(composeError(it))
                }
            }
            .doOnError(::doOnError)
            .subscribeOn(executionThread.get())
            .observeOn(postExecutionThread.get())
    }

    private fun doOnError(throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            throwable.printStackTrace()
        }
    }
}
