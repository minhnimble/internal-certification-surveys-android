package co.nimblehq.extension

import co.nimblehq.data.lib.common.DATE_FORMAT_FULL_DISPLAY
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

fun Date.toDisplayFormat(format: String = DATE_FORMAT_FULL_DISPLAY): String {
    return try {
        SimpleDateFormat(format, Locale.getDefault()).format(this)
    } catch (e: Exception) {
        Timber.d(e, "Unable to parse the provided format")
        ""
    }
}
