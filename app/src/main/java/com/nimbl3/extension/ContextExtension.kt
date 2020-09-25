package com.nimbl3.extension

import android.content.Context
import androidx.annotation.*

fun Context.getResourceName(@IdRes resId: Int?): String? =
    resId?.let { resources.getResourceName(resId) }