package co.nimblehq.data.error

import androidx.annotation.StringRes

open class AppError(
    cause: Throwable?,
    open val readableMessage: String? = null,
    @StringRes open val readableMessageRes: Int? = null
) : Throwable(cause) {

    val code: Int?
        get() = (cause as? JsonApiException)?.code()
}

class Ignored(cause: Throwable?) : AppError(cause, null, null)

