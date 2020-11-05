package co.nimblehq.data.error

import androidx.annotation.StringRes
import co.nimblehq.R

sealed class UserError(
    cause: Throwable?,
    @StringRes readableMessageRes: Int? = null
) : AppError(cause, (cause as? JsonApiException)?.error?.detail, readableMessageRes) {

    class LoadCurrentUserInfoError(cause: Throwable?) : UserError(
        cause, R.string.general_get_current_user_info_error
    )
}
