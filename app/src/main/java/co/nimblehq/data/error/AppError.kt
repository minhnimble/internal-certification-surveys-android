package co.nimblehq.data.error

import androidx.annotation.StringRes
import retrofit2.HttpException

open class AppError(
    cause: Throwable?,
    open val readableMessage: String? = null,
    @StringRes open val readableMessageRes: Int? = null
) : Throwable(cause) {

    protected open val code: Int?
        get() = (cause as? JsonApiException)?.response?.code()
}

class Ignored(cause: Throwable?) : AppError(cause, null, null)

