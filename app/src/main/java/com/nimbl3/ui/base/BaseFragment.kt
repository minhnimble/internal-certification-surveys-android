package com.nimbl3.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.nimbl3.extension.userReadableMessage
import com.nimbl3.ui.common.Toaster
import com.nimbl3.ui.common.dialog.AppProgressDialog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

abstract class BaseFragment: Fragment() {

    private var disposables = CompositeDisposable()

    @Inject lateinit var loader: AppProgressDialog

    @Inject lateinit var toaster: Toaster

    protected abstract val layoutRes: Int

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (this as? BaseFragmentCallbacks)?.let {
            setupView()
            bindViewEvents()
            bindViewModel()
        }
    }

    override fun onDetach() {
        super.onDetach()
        disposables.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }

    protected fun Disposable.bindForDisposable() {
        disposables.add(this)
    }

    protected open fun displayError(error: Throwable) {
        val message = error.userReadableMessage(requireContext())
        toaster.display(message)
    }

    /**
     * @method to toggle to show/hide the app loading
     * @param cancelable should progress dialog cancel with outer touch default=true
     */
    fun toggleLoading(showLoading: Boolean,cancelable: Boolean = true) {
        if (showLoading) {
            hideLoading()
            loader.show()
            loader.setCanceledOnTouchOutside(cancelable)
            loader.setCancelable(cancelable)
        } else {
            hideLoading()
        }
    }

    private fun hideLoading() {
        if (loader.isShowing)
            loader.dismiss()
    }
}
