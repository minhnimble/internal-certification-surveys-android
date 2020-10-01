package co.nimblehq.data.error

class LoginError(
    cause: Throwable?
) : AppError(
    cause = cause,
    readableMessage = (cause as? JsonApiException)?.error?.detail
)
