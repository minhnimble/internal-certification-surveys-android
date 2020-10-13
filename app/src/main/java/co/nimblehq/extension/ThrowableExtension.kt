package co.nimblehq.extension

import android.content.Context
import co.nimblehq.R
import co.nimblehq.data.error.AppError

fun Throwable.userReadableMessage(context: Context): String {
    return when (this) {
        is AppError -> {
            readableMessage ?: context.getString(readableMessageRes ?: R.string.general_unknown_error)
        }
        else -> context.getString(R.string.general_unknown_error)
    }
}
