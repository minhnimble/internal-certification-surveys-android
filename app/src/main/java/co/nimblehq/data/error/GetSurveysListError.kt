package co.nimblehq.data.error

class GetSurveysListError(
    cause: Throwable?
) : AppError(
    cause = cause,
    readableMessage = (cause as? JsonApiException)?.error?.detail
)
