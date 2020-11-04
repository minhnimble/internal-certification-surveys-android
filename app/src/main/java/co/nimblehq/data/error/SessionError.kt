package co.nimblehq.data.error

import androidx.annotation.StringRes
import co.nimblehq.R

sealed class SessionError(
    cause: Throwable? = null,
    @StringRes readableMessageRes: Int? = null
) : AppError(cause, (cause as? JsonApiException)?.error?.detail, readableMessageRes) {

    class LoginError(cause: Throwable? = null) : SessionError(
        cause, R.string.general_login_error
    )

    class LogoutError(cause: Throwable? = null) : SessionError(
        cause, R.string.general_logout_error
    )

    class RefreshTokenError(cause: Throwable? = null) : SessionError(
        cause, R.string.general_refresh_token_error
    )

    class TokenExpiredError(cause: Throwable? = null) : SessionError(
        cause, R.string.general_session_expired_error
    )
}
