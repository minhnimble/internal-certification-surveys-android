package co.nimblehq.data.error

import co.nimblehq.R

class TokenExpiredError(
    cause: Throwable?
) : AppError(
    cause = cause,
    readableMessageRes = R.string.session_expired_error
)
