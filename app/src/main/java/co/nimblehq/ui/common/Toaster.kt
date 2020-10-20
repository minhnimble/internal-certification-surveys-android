package co.nimblehq.ui.common

import android.content.Context
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.makeText
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class Toaster @Inject constructor(@ActivityContext private val context: Context) {

    private var toast: Toast? = null

    fun display(message: String, duration: Int = LENGTH_LONG) {
        toast?.cancel()
        toast = makeText(context, message, duration).also { it.show() }
    }
}
