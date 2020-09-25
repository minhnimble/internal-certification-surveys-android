package com.nimbl3.usecase.base

import com.nimbl3.BuildConfig
import com.nimbl3.data.errors.AppError
import io.reactivex.Single
import com.nimbl3.data.lib.schedulers.RxScheduler

abstract class SingleUseCase<in UseCaseInput, T>(
        private val executionThread: RxScheduler,
        private val postExecutionThread: RxScheduler,
        defaultError: (Throwable) -> AppError
) : BaseUseCase(defaultError) {

    protected abstract fun create(input: UseCaseInput): Single<T>

    fun execute(input: UseCaseInput): Single<T> {
        return create(input)
            .onErrorResumeNext {
                if (isCanceledException(it)) {
                    Single.never()
                } else {
                    Single.error<T>(composeError(it))
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
