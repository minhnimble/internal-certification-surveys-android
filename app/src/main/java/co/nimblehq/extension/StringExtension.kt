package co.nimblehq.extension

import androidx.core.util.PatternsCompat

fun String.isEmail() = PatternsCompat.EMAIL_ADDRESS.matcher(this).matches()

