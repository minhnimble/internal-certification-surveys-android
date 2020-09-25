package com.nimbl3.data.lib.rxjava.transformers

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.functions.Consumer

class ObservableErrorTransformer<T>(
    private val errorConsumer: Consumer<Throwable>
) : ObservableTransformer<T, T> {

    override fun apply(source: Observable<T>): ObservableSource<T> {
        return source
            .doOnError(errorConsumer::accept)
            .onErrorResumeNext(Observable.empty())
    }
}
