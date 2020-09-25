package com.nimbl3.ui.base

import androidx.appcompat.app.AppCompatActivity
import com.nimbl3.extension.userReadableMessage
import com.nimbl3.ui.common.Toaster
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

abstract class BaseActivity: AppCompatActivity() {

    @Inject lateinit var toaster: Toaster

    private var disposables = CompositeDisposable()

    protected fun Disposable.bindForDisposable() {
        disposables.add(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    fun displayError(error: Throwable) {
        val message = error.userReadableMessage(this)
        toaster.display(message)
    }
}
