package co.nimblehq.data.error

import androidx.annotation.StringRes
import co.nimblehq.R

sealed class SurveyError(
    cause: Throwable?,
    @StringRes readableMessageRes: Int? = null
) : AppError(cause, (cause as? JsonApiException)?.error?.detail, readableMessageRes) {
    
    class GetSurveysError(cause: Throwable?) : SurveyError(
        cause, R.string.general_get_surveys_error
    )

    class NoMoreSurveysError(cause: Throwable?) : SurveyError(
        cause, R.string.general_no_more_surveys_error
    )

    class DeleteLocalSurveysError(cause: Throwable?) : SurveyError(
        cause, R.string.general_delete_local_surveys_error
    )

    class GetSurveyDetailsError(cause: Throwable?) : SurveyError(
        cause, R.string.general_get_survey_details_error
    )
}
