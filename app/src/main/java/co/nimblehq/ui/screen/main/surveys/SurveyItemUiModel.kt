package co.nimblehq.ui.screen.main.surveys

import android.os.Parcelable
import co.nimblehq.data.model.Survey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SurveyItemUiModel(
    val id: String = "",
    val description: String = "",
    val header: String = "",
    val imageUrl: String = ""
): Parcelable

fun Survey.toSurveyItemUiModel() = SurveyItemUiModel(
    id,
    description,
    title,
    highResImageUrl
)

fun List<Survey>.toSurveyItemUiModels() = map { it.toSurveyItemUiModel() }
