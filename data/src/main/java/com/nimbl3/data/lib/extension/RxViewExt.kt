package com.nimbl3.data.lib.extension

import android.util.Log
import android.view.View
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.disposables.Disposable

/**
 * Subscribe to the [View]'s OnClick event, with debounce timeout of 200ms.
 * When an error happens, the stream isn't stopped, it just notifies the error through [onError] callback
 */
fun View.subscribeOnClick(onNext: () -> Unit, onError: (Throwable) -> Unit): Disposable {
    return clicks()
        .debounceAndSubscribe(
            onNext = { onNext() },
            onError = onError
        )
}

/**
 * Subscribe to the [View]'s OnClick event, with debounce timeout of 200ms.
 * When an error happens, the stream isn't stopped, it just prints out the error with [Log.e]
 */
fun View.subscribeOnClick(onNext: () -> Unit): Disposable {
    return clicks().debounceAndSubscribe { onNext() }
}
