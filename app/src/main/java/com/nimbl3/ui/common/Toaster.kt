package com.nimbl3.ui.common

import android.app.Activity
import android.widget.Toast
import android.widget.Toast.*
import javax.inject.Inject

class Toaster @Inject constructor(private val activity: Activity) {

    private var toast: Toast? = null

    fun display(message: String) {
        toast?.cancel()
        toast = makeText(activity, message, LENGTH_LONG).also { it.show() }
    }
}
