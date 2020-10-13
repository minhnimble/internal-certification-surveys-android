package co.nimblehq.data.error

import androidx.annotation.StringRes
import co.nimblehq.R

sealed class SurveyError(
    cause: Throwable?,
    @StringRes readableMessageRes: Int? = null
) : AppError(cause, (cause as? JsonApiException)?.error?.detail, readableMessageRes) {
    
    class GetSurveysListError(cause: Throwable?) : SurveyError(
        cause, R.string.get_surveys_list_error
    )

    // TODO: Add new class for handling error when getting survey's details
}
