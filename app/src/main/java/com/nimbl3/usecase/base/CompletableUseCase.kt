package com.nimbl3.usecase.base

import com.nimbl3.BuildConfig
import com.nimbl3.data.errors.AppError
import io.reactivex.Completable
import com.nimbl3.data.lib.schedulers.RxScheduler

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
