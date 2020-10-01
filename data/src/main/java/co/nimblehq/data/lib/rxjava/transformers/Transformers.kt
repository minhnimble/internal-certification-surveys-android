package co.nimblehq.data.lib.rxjava.transformers

import io.reactivex.Observable

object Transformers {

    /**
     * Emits the latest value of the `source` Observable whenever the `when`
     * Observable emits.
     */
    fun <S, T> takeWhen(`when`: Observable<T>): TakeWhenTransformer<S, T> {
        return TakeWhenTransformer(`when`)
    }

    /**
     * Prevents an flowable from erroring by chaining `onErrorResumeNext`.
     * // TODO: check out for `materalize()` as an alternative choice.
     */
    fun <T> flowableNeverError(): FlowableNeverErrorTransformer<T> {
        return FlowableNeverErrorTransformer()
    }
}
