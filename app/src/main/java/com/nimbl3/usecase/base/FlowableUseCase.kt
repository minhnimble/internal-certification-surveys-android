package com.nimbl3.usecase.base

import com.nimbl3.BuildConfig
import com.nimbl3.data.errors.AppError
import io.reactivex.Flowable
import com.nimbl3.data.lib.schedulers.RxScheduler

abstract class FlowableUseCase<in UseCaseInput, T>(
        private val executionThread: RxScheduler,
        private val postExecutionThread: RxScheduler,
        defaultError: (Throwable) -> AppError
) : BaseUseCase(defaultError) {

    protected abstract fun create(input: UseCaseInput): Flowable<T>

    fun execute(input: UseCaseInput): Flowable<T> {
        return create(input)
            .onErrorResumeNext { error: Throwable ->
                if (isCanceledException(error)) {
                    Flowable.never()
                } else {
                    Flowable.error<T>(composeError(error))
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
