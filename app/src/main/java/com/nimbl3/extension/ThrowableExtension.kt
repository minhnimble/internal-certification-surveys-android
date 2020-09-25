package com.nimbl3.extension

import android.content.Context
import com.nimbl3.R
import com.nimbl3.data.errors.AppError

fun Throwable.userReadableMessage(context: Context): String {
    return when (this) {
        is AppError -> {
            readableMessage ?: context.getString(readableMessageRes ?: R.string.error_generic)
        }
        else -> context.getString(R.string.error_generic)
    }
}