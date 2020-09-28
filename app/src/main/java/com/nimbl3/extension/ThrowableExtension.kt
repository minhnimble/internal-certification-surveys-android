package com.nimbl3.extension

import android.content.Context
import com.nimbl3.R

fun Throwable.userReadableMessage(context: Context): String {
    return context.getString(R.string.error_generic)
}