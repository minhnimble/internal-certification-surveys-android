package co.nimblehq.extension

import co.nimblehq.data.lib.common.dd_MMM_yyyy_hh_mm_a
import java.text.SimpleDateFormat
import java.util.*

fun Date.getString(format: String = dd_MMM_yyyy_hh_mm_a): String {
    val sdf = SimpleDateFormat(format)
    return sdf.format(this)
}
