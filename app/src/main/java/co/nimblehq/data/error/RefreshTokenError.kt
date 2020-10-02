package co.nimblehq.data.error


class RefreshTokenError(
    cause: Throwable? = null
) : AppError(cause, null, null) {
    val hasInvalidGrantCode = (cause as? JsonApiException)?.hasInvalidGrantCode ?: false
}
