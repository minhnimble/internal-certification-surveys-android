package co.nimblehq.extension

import android.content.Context
import co.nimblehq.R

fun Throwable.userReadableMessage(context: Context): String {
    return context.getString(R.string.error_generic) // TODO: Add more error messages handler here
}
