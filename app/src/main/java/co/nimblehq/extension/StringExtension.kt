package co.nimblehq.extension

import androidx.core.util.PatternsCompat
import java.text.SimpleDateFormat
import java.util.*

fun String.getCurrentDate(): String {
    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    return sdf.format(Date())
}

fun String.isEmail() = PatternsCompat.EMAIL_ADDRESS.matcher(this).matches()

