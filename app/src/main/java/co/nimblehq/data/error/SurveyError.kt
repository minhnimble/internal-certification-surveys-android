package co.nimblehq.data.error

import androidx.annotation.StringRes
import co.nimblehq.R
import retrofit2.HttpException

sealed class SurveyError(
    cause: Throwable?,
    @StringRes readableMessageRes: Int? = null
) : AppError(cause, (cause as? JsonApiException)?.error?.detail, readableMessageRes) {
    
    class GetSurveysListError(cause: Throwable?) : SurveyError(
        cause, R.string.general_get_surveys_list_error
    ) {
        val isNotFound: Boolean
            get() = (cause as? HttpException)?.code() == 404
    }

    class NoMoreSurveysListError(cause: Throwable?) : SurveyError(
        cause, R.string.general_no_more_surveys_list_error
    )

    // TODO: Add new class for handling error when getting survey's details
}
